package cl.truchoradios.chile.cast

import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.SimpleBasePlayer
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

@UnstableApi
class CastPlayerSwitcher(
    private val localPlayer: ExoPlayer,
    private val castPlayer: CastPlayer,
) : SimpleBasePlayer(localPlayer.applicationLooper) {

    private var currentPlayer: Player = localPlayer

    val isCasting: Boolean
        get() = currentPlayer === castPlayer

    private val forwardingListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (player === currentPlayer) {
                invalidateState()
            }
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
    }

    private fun switchTo(newPlayer: Player) {
        if (currentPlayer === newPlayer) return

        val oldPlayer = currentPlayer
        val mediaItems = mutableListOf<MediaItem>()
        for (i in 0 until oldPlayer.mediaItemCount) {
            mediaItems.add(oldPlayer.getMediaItemAt(i))
        }
        val playWhenReady = oldPlayer.playWhenReady

        oldPlayer.stop()
        oldPlayer.clearMediaItems()

        currentPlayer = newPlayer

        if (mediaItems.isNotEmpty()) {
            newPlayer.setMediaItems(mediaItems)
            newPlayer.prepare()
            newPlayer.playWhenReady = playWhenReady
        }

        invalidateState()
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
        currentPlayer.volume = volume
        return Futures.immediateVoidFuture()
    }

    override fun handleRelease(): ListenableFuture<*> {
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
