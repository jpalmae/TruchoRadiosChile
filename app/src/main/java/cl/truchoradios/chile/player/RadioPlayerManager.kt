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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class PlaybackState {
    IDLE, BUFFERING, PLAYING, ERROR
}

@Singleton
class RadioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val userAgent = "TruchoRadiosChile/1.0 (Android; Radio Player)"

    private val dataSourceFactory = DefaultHttpDataSource.Factory()
        .setUserAgent(userAgent)
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

    private val _canSeekBack = MutableStateFlow(false)
    val canSeekBack: StateFlow<Boolean> = _canSeekBack

    private val _rewindSeconds = MutableStateFlow(0)
    val rewindSeconds: StateFlow<Int> = _rewindSeconds

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) {
                    _playbackState.value = PlaybackState.PLAYING
                    _canSeekBack.value = player.isCurrentMediaItemSeekable
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _isBuffering.value = playbackState == Player.STATE_BUFFERING
                when (playbackState) {
                    Player.STATE_BUFFERING -> _playbackState.value = PlaybackState.BUFFERING
                    Player.STATE_READY -> {
                        _error.value = null
                        if (_isPlaying.value) {
                            _playbackState.value = PlaybackState.PLAYING
                        }
                        _canSeekBack.value = player.isCurrentMediaItemSeekable
                    }
                    Player.STATE_ENDED -> _playbackState.value = PlaybackState.IDLE
                }
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
        _rewindSeconds.value = 0
        _canSeekBack.value = false

        player.stop()
        player.clearMediaItems()

        val metadataBuilder = MediaMetadata.Builder()
            .setTitle(radio.name)
            .setArtist(radio.genres.joinToString(", ").ifBlank { "Radio Chilena" })

        if (radio.imageUrl.isNotBlank() && radio.imageUrl.startsWith("http")) {
            metadataBuilder.setArtworkUri(radio.imageUrl.toUri())
        }

        val mediaItem = MediaItem.Builder()
            .setUri(radio.streamUrl.toUri())
            .setMediaMetadata(metadataBuilder.build())
            .setLiveConfiguration(
                MediaItem.LiveConfiguration.Builder()
                    .setMaxOffsetMs(60_000L)
                    .setTargetOffsetMs(30_000L)
                    .build()
            )
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    fun seekBackSeconds(seconds: Int) {
        if (!player.isCurrentMediaItemSeekable) return
        val currentPosition = player.currentPosition
        val newPosition = currentPosition - (seconds * 1000L)
        val minPosition = player.duration.coerceAtMost(0L)
        player.seekTo(newPosition.coerceAtLeast(minPosition))
        _rewindSeconds.value = _rewindSeconds.value + seconds
    }

    fun seekToLive() {
        if (player.isCurrentMediaItemSeekable) {
            player.seekToDefaultPosition()
            _rewindSeconds.value = 0
        }
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
        _isPlaying.value = false
        _isBuffering.value = false
        _currentRadio.value = null
        _playbackState.value = PlaybackState.IDLE
        _rewindSeconds.value = 0
        _canSeekBack.value = false
    }

    fun getCurrentRadio(): Radio? = _currentRadio.value
}
