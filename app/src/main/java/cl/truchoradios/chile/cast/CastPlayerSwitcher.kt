package cl.truchoradios.chile.cast

import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.SimpleBasePlayer
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.gms.cast.Cast
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@UnstableApi
class CastPlayerSwitcher(
    private val localPlayer: ExoPlayer,
    private val castPlayer: CastPlayer,
) : SimpleBasePlayer(localPlayer.applicationLooper) {

    private var currentPlayer: Player = if (castPlayer.isCastSessionAvailable) {
        castPlayer
    } else {
        localPlayer
    }

    val isCasting: Boolean
        get() = currentPlayer === castPlayer

    private val _isCastingFlow = MutableStateFlow(isCasting)
    val isCastingFlow: StateFlow<Boolean> = _isCastingFlow.asStateFlow()

    val volumeFlow = MutableStateFlow(localPlayer.volume)

    private val forwardingListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (player === currentPlayer) {
                if (events.contains(Player.EVENT_VOLUME_CHANGED)) {
                    volumeFlow.value = currentVolume()
                }
                invalidateState()
            }
        }
    }

    private val remoteMediaClient: RemoteMediaClient?
        get() = runCatching {
            CastContext.getSharedInstance()?.sessionManager?.currentCastSession?.remoteMediaClient
        }.getOrNull()

    private val castSession: CastSession?
        get() = runCatching {
            CastContext.getSharedInstance()?.sessionManager?.currentCastSession as? CastSession
        }.getOrNull()

    private var activeCastSession: CastSession? = null
    private var activeRemoteMediaClient: RemoteMediaClient? = null

    private val castVolumeCallback = object : RemoteMediaClient.Callback() {
        override fun onStatusUpdated() {
            if (isCasting) {
                activeRemoteMediaClient?.mediaStatus?.streamVolume?.let {
                    volumeFlow.value = it.toFloat()
                }
            }
        }
    }

    private val castListener = object : Cast.Listener() {
        override fun onVolumeChanged() {
            if (isCasting) {
                activeCastSession?.let {
                    runCatching { it.volume.toFloat() }.onSuccess { v ->
                        volumeFlow.value = v
                    }
                }
            }
        }
    }

    private fun currentVolume(): Float {
        return if (isCasting) {
            runCatching { activeCastSession?.volume?.toFloat() }.getOrNull()
                ?: activeRemoteMediaClient?.mediaStatus?.streamVolume?.toFloat()
                ?: volumeFlow.value
        } else {
            localPlayer.volume
        }
    }

    init {
        localPlayer.addListener(forwardingListener)
        castPlayer.addListener(forwardingListener)
        castPlayer.setSessionAvailabilityListener(object : SessionAvailabilityListener {
            override fun onCastSessionAvailable() {
                switchTo(castPlayer)
            }

            override fun onCastSessionUnavailable() {
                switchTo(localPlayer)
            }
        })
        // A Cast session can already be restored before this listener is installed.
        switchTo(if (castPlayer.isCastSessionAvailable) castPlayer else localPlayer)
    }

    private fun switchTo(newPlayer: Player) {
        if (currentPlayer === newPlayer) {
            if (newPlayer === castPlayer) attachCastSession() else detachCastSession()
            _isCastingFlow.value = newPlayer === castPlayer
            volumeFlow.value = currentVolume()
            invalidateState()
            return
        }

        val oldPlayer = currentPlayer
        val mediaItems = mutableListOf<MediaItem>()
        for (i in 0 until oldPlayer.mediaItemCount) {
            mediaItems.add(oldPlayer.getMediaItemAt(i))
        }
        val playWhenReady = oldPlayer.playWhenReady
        val shouldAdoptExistingCast = newPlayer === castPlayer &&
            (castPlayer.mediaItemCount > 0 || remoteMediaClient?.hasMediaSession() == true)

        currentPlayer = newPlayer
        _isCastingFlow.value = newPlayer === castPlayer
        oldPlayer.stop()
        oldPlayer.clearMediaItems()

        if (newPlayer === castPlayer) {
            attachCastSession()
        } else {
            detachCastSession()
        }

        volumeFlow.value = if (newPlayer === castPlayer) {
            currentVolume()
        } else {
            localPlayer.volume
        }

        // Do not reload an existing remote queue when the app is reopened.
        if (!shouldAdoptExistingCast && mediaItems.isNotEmpty()) {
            newPlayer.setMediaItems(mediaItems)
            newPlayer.prepare()
            newPlayer.playWhenReady = playWhenReady
        }

        invalidateState()
    }

    private fun attachCastSession() {
        val session = castSession
        val mediaClient = session?.remoteMediaClient
        if (session === activeCastSession && mediaClient === activeRemoteMediaClient) return

        detachCastSession()
        activeCastSession = session
        activeRemoteMediaClient = mediaClient
        runCatching { session?.addCastListener(castListener) }
        mediaClient?.registerCallback(castVolumeCallback)
    }

    private fun detachCastSession() {
        activeRemoteMediaClient?.unregisterCallback(castVolumeCallback)
        runCatching { activeCastSession?.removeCastListener(castListener) }
        activeRemoteMediaClient = null
        activeCastSession = null
    }

    override fun getState(): State {
        val playlist = mutableListOf<MediaItemData>()
        for (i in 0 until currentPlayer.mediaItemCount) {
            val item = currentPlayer.getMediaItemAt(i)
            playlist.add(
                MediaItemData.Builder(item.mediaId)
                    .setMediaItem(item)
                    .setDurationUs(C.TIME_UNSET)
                    .build()
            )
        }

        val playbackState = if (playlist.isEmpty() &&
            currentPlayer.playbackState != Player.STATE_IDLE &&
            currentPlayer.playbackState != Player.STATE_ENDED
        ) {
            Player.STATE_IDLE
        } else {
            currentPlayer.playbackState
        }

        val isLoading = currentPlayer.isLoading &&
            playbackState != Player.STATE_IDLE &&
            playbackState != Player.STATE_ENDED

        return State.Builder()
            .setAvailableCommands(AVAILABLE_COMMANDS)
            .setPlayWhenReady(
                currentPlayer.playWhenReady,
                Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST
            )
            .setPlaybackState(playbackState)
            .setPlaybackParameters(currentPlayer.playbackParameters)
            .setPlayerError(currentPlayer.playerError)
            .setIsLoading(isLoading)
            .setPlaylist(playlist)
            .setPlaylistMetadata(currentPlayer.playlistMetadata)
            .setVolume(volumeFlow.value)
            .setContentPositionMs(currentPlayer.contentPosition.coerceAtLeast(0))
            .build()
    }

    override fun handleSetMediaItems(
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ): ListenableFuture<*> {
        currentPlayer.setMediaItems(mediaItems, startIndex, startPositionMs)
        return Futures.immediateVoidFuture()
    }

    override fun handlePrepare(): ListenableFuture<*> {
        currentPlayer.prepare()
        return Futures.immediateVoidFuture()
    }

    override fun handleSetPlayWhenReady(playWhenReady: Boolean): ListenableFuture<*> {
        currentPlayer.playWhenReady = playWhenReady
        return Futures.immediateVoidFuture()
    }

    override fun handleStop(): ListenableFuture<*> {
        currentPlayer.stop()
        return Futures.immediateVoidFuture()
    }

    override fun handleSetVolume(volume: Float): ListenableFuture<*> {
        if (currentPlayer === castPlayer) {
            // CastPlayer.setVolume de media3 es un no-op: el volumen del
            // dispositivo se controla via RemoteMediaClient.
            activeRemoteMediaClient?.setStreamVolume(volume.toDouble())
            volumeFlow.value = volume
        } else {
            currentPlayer.volume = volume
        }
        return Futures.immediateVoidFuture()
    }

    override fun handleRelease(): ListenableFuture<*> {
        detachCastSession()
        localPlayer.removeListener(forwardingListener)
        castPlayer.removeListener(forwardingListener)
        castPlayer.setSessionAvailabilityListener(null)
        localPlayer.release()
        castPlayer.release()
        return Futures.immediateVoidFuture()
    }

    private companion object {
        val AVAILABLE_COMMANDS: Player.Commands = Player.Commands.Builder()
            .addAll(
                Player.COMMAND_PLAY_PAUSE,
                Player.COMMAND_PREPARE,
                Player.COMMAND_STOP,
                Player.COMMAND_SET_MEDIA_ITEM,
                Player.COMMAND_GET_CURRENT_MEDIA_ITEM,
                Player.COMMAND_GET_TIMELINE,
                Player.COMMAND_GET_METADATA,
                Player.COMMAND_GET_VOLUME,
                Player.COMMAND_SET_VOLUME,
            )
            .build()
    }
}
