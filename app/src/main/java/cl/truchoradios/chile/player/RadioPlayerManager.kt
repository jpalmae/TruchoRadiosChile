package cl.truchoradios.chile.player

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.media3.cast.CastPlayer
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.TeeAudioProcessor
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import cl.truchoradios.chile.cast.CastPlayerSwitcher
import cl.truchoradios.chile.data.local.entity.toDomain
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.domain.model.StreamType
import cl.truchoradios.chile.media.resolveArtworkUri
import cl.truchoradios.chile.service.RadioPlayerService
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class PlaybackState {
    IDLE, BUFFERING, PLAYING, PAUSED, ERROR
}

@androidx.annotation.OptIn(UnstableApi::class)
@Singleton
class RadioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: RadioRepositoryImpl,
) {
    private val dataSourceFactory = DefaultHttpDataSource.Factory()
        .setUserAgent("TruchoRadiosChile/1.0 (Android; Radio Player)")
        .setConnectTimeoutMs(15000)
        .setReadTimeoutMs(15000)
        .setAllowCrossProtocolRedirects(true)

    private val spectrumAnalyzer = AudioSpectrumAnalyzer()
    val spectrumBands: StateFlow<List<Float>> = spectrumAnalyzer.bands

    private val renderersFactory = object : DefaultRenderersFactory(context) {
        override fun buildAudioSink(
            context: Context,
            enableFloatOutput: Boolean,
            enableAudioTrackPlaybackParams: Boolean,
        ): AudioSink = DefaultAudioSink.Builder(context)
            .setEnableFloatOutput(enableFloatOutput)
            .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
            .setAudioProcessors(arrayOf(TeeAudioProcessor(spectrumAnalyzer)))
            .build()
    }

    val player: ExoPlayer = ExoPlayer.Builder(context, renderersFactory)
        .setAudioAttributes(AudioAttributes.DEFAULT, true)
        .setHandleAudioBecomingNoisy(true)
        .setMediaSourceFactory(
            DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory)
        )
        .build()

    val castContext: CastContext? = runCatching {
        CastContext.getSharedInstance(context)
    }.getOrNull()

    private val castPlayer: CastPlayer? = castContext?.let { ctx ->
        runCatching { CastPlayer(ctx) }.getOrNull()
    }

    @UnstableApi
    private val castSwitcher: CastPlayerSwitcher? = castPlayer?.let {
        CastPlayerSwitcher(player, it)
    }

    val isCastAvailable: Boolean get() = castSwitcher != null

    @UnstableApi
    val sessionPlayer: Player = castSwitcher ?: player

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currentRadio = MutableStateFlow<Radio?>(null)
    val currentRadio: StateFlow<Radio?> = _currentRadio

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _isCasting = MutableStateFlow(false)
    val isCasting: StateFlow<Boolean> = _isCasting

    private val _audioSessionId = MutableStateFlow(0)
    val audioSessionId: StateFlow<Int> = _audioSessionId

    // Sleep timer
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var sleepTimerJob: Job? = null

    private val _sleepTimerRemaining = MutableStateFlow(0L)
    val sleepTimerRemaining: StateFlow<Long> = _sleepTimerRemaining

    private val _volume = MutableStateFlow(1f)
    val volume: StateFlow<Float> = _volume

    // MediaController for foreground service + notification
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    init {
        if (castSwitcher != null) {
            scope.launch {
                castSwitcher.volumeFlow.collect { _volume.value = it }
            }
        } else {
            player.addListener(object : Player.Listener {
                override fun onVolumeChanged(volume: Float) {
                    _volume.value = volume
                }
            })
        }

        sessionPlayer.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                _isCasting.value = castSwitcher?.isCasting == true
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                _isPlaying.value = playing
                if (playing) _playbackState.value = PlaybackState.PLAYING
            }

            override fun onPlaybackStateChanged(state: Int) {
                _isBuffering.value = state == Player.STATE_BUFFERING
                when (state) {
                    Player.STATE_BUFFERING -> _playbackState.value = PlaybackState.BUFFERING
                    Player.STATE_READY -> {
                        _error.value = null
                        if (_isPlaying.value) _playbackState.value = PlaybackState.PLAYING
                    }
                    Player.STATE_ENDED -> _playbackState.value = PlaybackState.IDLE
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val mediaId = mediaItem?.mediaId.orEmpty()
                if (mediaId.isNotEmpty() && _currentRadio.value?.id != mediaId) {
                    scope.launch {
                        repository.getRadioById(mediaId)?.let { entity ->
                            _currentRadio.value = entity.toDomain()
                            repository.addRecent(mediaId)
                        }
                    }
                } else if (mediaId.isNotEmpty()) {
                    scope.launch { repository.addRecent(mediaId) }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                _error.value = "Error al reproducir: ${error.message}"
                _isPlaying.value = false
                _isBuffering.value = false
                _playbackState.value = PlaybackState.ERROR
            }
        })

        player.addListener(object : Player.Listener {
            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                _audioSessionId.value = audioSessionId
            }
        })

        // Connect to MediaSession service for foreground + notification
        initMediaController()
    }

    private fun initMediaController() {
        val sessionToken = SessionToken(context, android.content.ComponentName(context, RadioPlayerService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken)
            .buildAsync()
        mediaControllerFuture?.addListener({
            try {
                mediaController = mediaControllerFuture?.get()
            } catch (e: Exception) {
                // Controller connection failed — playback still works, just no notification
            }
        }, { it.run() })
    }

    fun play(radio: Radio) {
        _error.value = null
        _isBuffering.value = true
        _currentRadio.value = radio
        _playbackState.value = PlaybackState.BUFFERING

        sessionPlayer.stop()
        sessionPlayer.clearMediaItems()

        val metadata = MediaMetadata.Builder()
            .setTitle(radio.name)
            .setArtist(radio.genres.joinToString(", ").ifBlank { "Radio Chilena" })

        metadata.setArtworkUri(context.resolveArtworkUri(radio.imageUrl))

        val mediaItem = MediaItem.Builder()
            .setMediaId(radio.id)
            .setUri(radio.streamUrl.toUri())
            .setMimeType(
                when (radio.streamType) {
                    StreamType.MP3 -> MimeTypes.AUDIO_MPEG
                    StreamType.AAC -> MimeTypes.AUDIO_AAC
                    StreamType.HLS -> MimeTypes.APPLICATION_M3U8
                    StreamType.OGG -> MimeTypes.AUDIO_OGG
                }
            )
            .setMediaMetadata(metadata.build())
            .build()

        sessionPlayer.setMediaItem(mediaItem)
        sessionPlayer.prepare()
        sessionPlayer.playWhenReady = true
    }

    fun pause() {
        sessionPlayer.playWhenReady = false
        _isPlaying.value = false
        _playbackState.value = PlaybackState.PAUSED
    }

    fun resume() {
        sessionPlayer.playWhenReady = true
        _playbackState.value = PlaybackState.PLAYING
    }

    fun stop() {
        sessionPlayer.stop()
        sessionPlayer.clearMediaItems()
        _isPlaying.value = false
        _isBuffering.value = false
        _currentRadio.value = null
        _playbackState.value = PlaybackState.IDLE
        cancelSleepTimer()
    }

    fun setVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        sessionPlayer.volume = clamped
        _volume.value = clamped
    }

    fun scheduleSleepTimer(minutes: Int) {
        cancelSleepTimer()
        _sleepTimerRemaining.value = minutes * 60_000L
        sleepTimerJob = scope.launch {
            while (_sleepTimerRemaining.value > 0) {
                delay(1000)
                _sleepTimerRemaining.value -= 1000
            }
            stop()
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        sleepTimerJob = null
        _sleepTimerRemaining.value = 0
    }

    fun getCurrentRadio(): Radio? = _currentRadio.value

    fun release() {
        sessionPlayer.release()
    }
}
