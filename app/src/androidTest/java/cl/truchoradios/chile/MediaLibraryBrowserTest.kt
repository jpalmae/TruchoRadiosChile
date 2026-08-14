package cl.truchoradios.chile

import android.content.ComponentName
import android.media.browse.MediaBrowser as PlatformMediaBrowser
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cl.truchoradios.chile.service.RadioPlayerService
import cl.truchoradios.chile.service.HttpBitmapLoader
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MediaLibraryBrowserTest {

    private lateinit var browserThread: HandlerThread
    private lateinit var browser: MediaBrowser

    @Before
    fun setUp() {
        browserThread = HandlerThread("browser").apply { start() }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val token = SessionToken(context, ComponentName(context, RadioPlayerService::class.java))
        val future = onBrowserThread {
            MediaBrowser.Builder(context, token)
                .setApplicationLooper(browserThread.looper)
                .buildAsync()
        }
        browser = future.get(15, TimeUnit.SECONDS)
    }

    @After
    fun tearDown() {
        onBrowserThread {
            browser.stop()
            browser.release()
        }
        browserThread.quitSafely()
    }

    private fun <T> onBrowserThread(block: () -> T): T {
        val latch = CountDownLatch(1)
        var result: Result<T>? = null
        Handler(browserThread.looper).post {
            result = runCatching(block)
            latch.countDown()
        }
        latch.await(30, TimeUnit.SECONDS)
        return result!!.getOrThrow()
    }

    private fun children(id: String, size: Int = 200) =
        onBrowserThread { browser.getChildren(id, 0, size, null) }.get(20, TimeUnit.SECONDS).value.orEmpty()

    @Test
    fun libraryRoot_and_topLevelFolders(): Unit = runBlocking {
        val root = onBrowserThread { browser.getLibraryRoot(null) }.get(20, TimeUnit.SECONDS)
        assertEquals("trucho_root", root.value?.mediaId)

        val folderIds = children("trucho_root").map { it.mediaId }
        assertEquals(
            listOf("favorites", "recent", "popular", "regions", "genres", "all"),
            folderIds
        )
    }

    @Test
    fun legacyBrowser_connectsAndGetsRoot() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val connected = CountDownLatch(1)
        var connectionFailed = false
        var rootId: String? = null
        lateinit var legacyBrowser: PlatformMediaBrowser

        instrumentation.runOnMainSync {
            legacyBrowser = PlatformMediaBrowser(
                context,
                ComponentName(context, RadioPlayerService::class.java),
                object : PlatformMediaBrowser.ConnectionCallback() {
                    override fun onConnected() {
                        rootId = legacyBrowser.root
                        connected.countDown()
                    }

                    override fun onConnectionFailed() {
                        connectionFailed = true
                        connected.countDown()
                    }
                },
                null
            )
            legacyBrowser.connect()
        }

        assertTrue("legacy browser did not connect in time", connected.await(10, TimeUnit.SECONDS))
        assertFalse("legacy browser connection failed", connectionFailed)
        assertEquals("trucho_root", rootId)

        instrumentation.runOnMainSync { legacyBrowser.disconnect() }
    }

    @Test
    fun regions_genres_and_radios_areBrowsable(): Unit = runBlocking {
        waitForSeed()

        val regions = children("regions")
        assertTrue((regions.size) > 5)

        val regionRadios = children(regions.first().mediaId)
        assertTrue(regionRadios.isNotEmpty())
        regionRadios.forEach {
            assertTrue(it.mediaMetadata.isPlayable == true)
        }

        val genres = children("genres")
        assertTrue(genres.size > 3)

        val all = children("all", 500)
        assertTrue(all.size > 200)
    }

    @Test
    fun search_returnsResults(): Unit = runBlocking {
        waitForSeed()
        val results = onBrowserThread { browser.getSearchResult("biobio", 0, 50, null) }
            .get(20, TimeUnit.SECONDS).value.orEmpty()
        assertTrue(results.isNotEmpty())
    }

    @Test
    fun children_respectRequestedPageSize(): Unit = runBlocking {
        waitForSeed()

        val firstPage = onBrowserThread { browser.getChildren("all", 0, 5, null) }
            .get(20, TimeUnit.SECONDS).value.orEmpty()
        val secondPage = onBrowserThread { browser.getChildren("all", 1, 5, null) }
            .get(20, TimeUnit.SECONDS).value.orEmpty()

        assertEquals(5, firstPage.size)
        assertEquals(5, secondPage.size)
        assertTrue(firstPage.all { it.mediaMetadata.artworkUri != null })
        val duplicatedIds = firstPage.map { it.mediaId }.toSet()
            .intersect(secondPage.map { it.mediaId }.toSet())
        assertTrue(duplicatedIds.isEmpty())
    }

    @OptIn(markerClass = [UnstableApi::class])
    @Test
    fun bundledArtwork_andFallback_canBeDecoded() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val loader = HttpBitmapLoader(context)

        val bundled = loader.loadBitmap(
            Uri.parse("file:///android_asset/radio_logos/carolina.gif")
        ).get(10, TimeUnit.SECONDS)
        val legacySvgReference = loader.loadBitmap(
            Uri.parse(
                "file:///android_asset/radio_logos/9632c4ae_0601_11e8_ae97_52543be04c81.svg"
            )
        ).get(10, TimeUnit.SECONDS)
        val fallback = loader.loadBitmap(
            Uri.parse("android.resource://${context.packageName}/drawable/trucho_logo")
        ).get(10, TimeUnit.SECONDS)

        assertTrue(bundled.width > 0 && bundled.height > 0)
        assertEquals(512, legacySvgReference.width)
        assertEquals(512, legacySvgReference.height)
        assertTrue(fallback.width > 0 && fallback.height > 0)
    }

    @Test
    fun selectedRadio_keepsArtworkMetadata(): Unit = runBlocking {
        waitForSeed()
        val radio = children("popular", 1).first()

        onBrowserThread { browser.setMediaItem(radio) }

        var selectedArtwork = onBrowserThread {
            browser.currentMediaItem?.mediaMetadata?.artworkUri
        }
        var waited = 0L
        while (selectedArtwork == null && waited < 3_000) {
            delay(100)
            waited += 100
            selectedArtwork = onBrowserThread {
                browser.currentMediaItem?.mediaMetadata?.artworkUri
            }
        }

        assertNotNull(selectedArtwork)
    }

    @Test
    fun playFromAuto_resolvesMediaIdAndPlays(): Unit = runBlocking {
        waitForSeed()

        val popular = children("popular", 5)
        val first = popular.firstOrNull()
        assertNotNull(first)

        onBrowserThread {
            browser.setMediaItem(first!!)
            browser.prepare()
            browser.play()
        }

        var waited = 0L
        while (waited < 20_000) {
            val playing = onBrowserThread { browser.isPlaying }
            if (playing) break
            delay(500)
            waited += 500
        }

        val state = onBrowserThread { browser.playbackState }
        val playWhenReady = onBrowserThread { browser.playWhenReady }
        assertTrue(
            "expected playback to start (state=$state)",
            onBrowserThread { browser.isPlaying } || state == Player.STATE_BUFFERING
        )
        assertTrue(playWhenReady)
    }

    private suspend fun waitForSeed() {
        var waited = 0L
        while (waited < 15_000) {
            if (children("all", 1).isNotEmpty()) return
            delay(500)
            waited += 500
        }
    }
}
