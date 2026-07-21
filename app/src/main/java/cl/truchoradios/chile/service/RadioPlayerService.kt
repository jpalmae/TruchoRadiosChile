package cl.truchoradios.chile.service

import android.content.Intent
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import cl.truchoradios.chile.data.local.entity.RadioEntity
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.player.RadioPlayerManager
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.SettableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RadioPlayerService : MediaLibraryService() {

    @Inject
    lateinit var playerManager: RadioPlayerManager

    @Inject
    lateinit var repository: RadioRepositoryImpl

    private var mediaLibrarySession: MediaLibrarySession? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @UnstableApi
    override fun onCreate() {
        super.onCreate()

        serviceScope.launch { repository.loadRadiosIfEmpty() }

        mediaLibrarySession = MediaLibrarySession.Builder(
            this,
            playerManager.sessionPlayer,
            LibraryCallback()
        )
            .setBitmapLoader(HttpBitmapLoader(this))
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaLibrarySession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaLibrarySession?.player
        if (player == null || !player.playWhenReady || player.playbackState == Player.STATE_ENDED) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaLibrarySession?.release()
        mediaLibrarySession = null
        serviceScope.cancel()
        super.onDestroy()
    }

    private inner class LibraryCallback : MediaLibrarySession.Callback {

        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> = future {
            LibraryResult.ofItem(
                MediaItem.Builder()
                    .setMediaId(ID_ROOT)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Trucho Radios Chile")
                            .setIsBrowsable(true)
                            .setIsPlayable(false)
                            .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                            .build()
                    )
                    .build(),
                params
            )
        }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = future {
            val items: List<MediaItem> = when {
                parentId == ID_ROOT -> listOf(
                    folder(ID_FAVORITES, "Favoritos"),
                    folder(ID_RECENT, "Recién escuchadas"),
                    folder(ID_POPULAR, "Populares"),
                    folder(ID_REGIONS, "Regiones"),
                    folder(ID_GENRES, "Géneros"),
                    folder(ID_ALL, "Todas las radios"),
                )
                parentId == ID_FAVORITES -> repository.getFavorites().first()
                    .mapNotNull { repository.getRadioById(it.radioId) }
                    .map { playableItem(it) }
                parentId == ID_RECENT -> repository.getRecent().first()
                    .mapNotNull { repository.getRadioById(it.radioId) }
                    .map { playableItem(it) }
                parentId == ID_POPULAR -> repository.getAllRadios().first()
                    .take(30)
                    .map { playableItem(it) }
                parentId == ID_ALL -> repository.getAllRadios().first()
                    .map { playableItem(it) }
                parentId == ID_REGIONS -> repository.getRegions().map {
                    folder("$PREFIX_REGION${it.name}", "${it.name} (${it.radioCount})")
                }
                parentId.startsWith(PREFIX_REGION) ->
                    repository.getRadiosByRegion(parentId.removePrefix(PREFIX_REGION)).first()
                        .map { playableItem(it) }
                parentId == ID_GENRES -> repository.getGenres().map {
                    folder("$PREFIX_GENRE${it.name}", "${it.name} (${it.radioCount})")
                }
                parentId.startsWith(PREFIX_GENRE) ->
                    repository.getRadiosByGenre(parentId.removePrefix(PREFIX_GENRE)).first()
                        .map { playableItem(it) }
                else -> emptyList()
            }
            LibraryResult.ofItemList(ImmutableList.copyOf(items), params)
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ): ListenableFuture<LibraryResult<MediaItem>> = future {
            val radio = repository.getRadioById(mediaId)
            if (radio != null) {
                LibraryResult.ofItem(playableItem(radio), null)
            } else {
                LibraryResult.ofError(SessionError.ERROR_BAD_VALUE)
            }
        }

        override fun onSearch(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            query: String,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<Void>> = future {
            val count = repository.searchRadios(query).first().size
            session.notifySearchResultChanged(browser, query, count, params)
            LibraryResult.ofVoid()
        }

        override fun onGetSearchResult(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            query: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = future {
            val items = repository.searchRadios(query).first().map { playableItem(it) }
            LibraryResult.ofItemList(ImmutableList.copyOf(items), params)
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> = future {
            mediaItems.map { item ->
                repository.getRadioById(item.mediaId)?.let { fullMediaItem(it) } ?: item
            }.toMutableList()
        }
    }

    private fun <T> future(block: suspend () -> T): ListenableFuture<T> {
        val result = SettableFuture.create<T>()
        serviceScope.launch {
            try {
                result.set(block())
            } catch (e: Exception) {
                result.setException(e)
            }
        }
        return result
    }

    private fun folder(id: String, title: String): MediaItem =
        MediaItem.Builder()
            .setMediaId(id)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setIsBrowsable(true)
                    .setIsPlayable(false)
                    .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                    .build()
            )
            .build()

    private fun playableItem(radio: RadioEntity): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setTitle(radio.name)
            .setArtist(
                listOf(radio.frequency, radio.city)
                    .filter { it.isNotBlank() }
                    .joinToString(" · ")
                    .ifBlank { "Radio Chilena" }
            )
            .setIsBrowsable(false)
            .setIsPlayable(true)
            .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)

        if (radio.imageUrl.startsWith("http")) {
            metadata.setArtworkUri(radio.imageUrl.toUri())
        }

        return MediaItem.Builder()
            .setMediaId(radio.id)
            .setMediaMetadata(metadata.build())
            .build()
    }

    private fun fullMediaItem(radio: RadioEntity): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setTitle(radio.name)
            .setArtist(radio.genres.ifBlank { "Radio Chilena" })

        if (radio.imageUrl.startsWith("http")) {
            metadata.setArtworkUri(radio.imageUrl.toUri())
        }

        return MediaItem.Builder()
            .setMediaId(radio.id)
            .setUri(radio.streamUrl.toUri())
            .setMimeType(
                when (radio.streamType) {
                    "AAC" -> MimeTypes.AUDIO_AAC
                    "HLS" -> MimeTypes.APPLICATION_M3U8
                    "OGG" -> MimeTypes.AUDIO_OGG
                    else -> MimeTypes.AUDIO_MPEG
                }
            )
            .setMediaMetadata(metadata.build())
            .build()
    }

    private companion object {
        const val ID_ROOT = "trucho_root"
        const val ID_FAVORITES = "favorites"
        const val ID_RECENT = "recent"
        const val ID_POPULAR = "popular"
        const val ID_REGIONS = "regions"
        const val ID_GENRES = "genres"
        const val ID_ALL = "all"
        const val PREFIX_REGION = "region/"
        const val PREFIX_GENRE = "genre/"
    }
}
