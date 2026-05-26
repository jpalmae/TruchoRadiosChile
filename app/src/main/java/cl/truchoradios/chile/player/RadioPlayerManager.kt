package cl.truchoradios.chile.player

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import cl.truchoradios.chile.domain.model.Radio
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

@Singleton
class RadioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataSourceFactory = DefaultHttpDataSource.Factory()
        .setUserAgent("TruchoRadiosChile/1.0 (Android; Radio Player)")
        .setConnectTimeoutMs(15000)
        .setReadTimeoutMs(15000)
        .setAllowCrossProtocolRedirects(true)

    val player: ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(AudioAttributes.DEFAULT, true)
        .setHandleAudioBecomingNoisy(true)
        .setMediaSourceFactory(
            DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory)
        )
        .build()

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

    private val _audioSessionId = MutableStateFlow(0)
    val audioSessionId: StateFlow<Int> = _audioSessionId

    // Sleep timer
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var sleepTimerJob: Job? = null

    private val _sleepTimerRemaining = MutableStateFlow(0L)
    val sleepTimerRemaining: StateFlow<Long> = _sleepTimerRemaining

    private val _volume = MutableStateFlow(1f)
    val volume: StateFlow<Float> = _volume

    init {
        player.addListener(object : Player.Listener {
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

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                _audioSessionId.value = audioSessionId
            }

            override fun onPlayerError(error: PlaybackException) {
                _error.value = "Error al reproducir: ${error.message}"
                _isPlaying.value = false
                _isBuffering.value = false
                _playbackState.value = PlaybackState.ERROR
            }
        })
    }

    fun play(radio: Radio) {
        _error.value = null
        _isBuffering.value = true
        _currentRadio.value = radio
        _playbackState.value = PlaybackState.BUFFERING

        player.stop()
        player.clearMediaItems()

        val metadata = MediaMetadata.Builder()
            .setTitle(radio.name)
            .setArtist(radio.genres.joinToString(", ").ifBlank { "Radio Chilena" })

        if (radio.imageUrl.isNotBlank() && radio.imageUrl.startsWith("http")) {
            metadata.setArtworkUri(radio.imageUrl.toUri())
        }

        val mediaItem = MediaItem.Builder()
            .setUri(radio.streamUrl.toUri())
            .setMediaMetadata(metadata.build())
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
        _isPlaying.value = false
        _playbackState.value = PlaybackState.PAUSED
    }

    fun resume() {
        player.playWhenReady = true
        _playbackState.value = PlaybackState.PLAYING
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
        _isPlaying.value = false
        _isBuffering.value = false
        _currentRadio.value = null
        _playbackState.value = PlaybackState.IDLE
        cancelSleepTimer()
    }

    fun setVolume(vol: Float) {
        player.volume = vol.coerceIn(0f, 1f)
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
}
