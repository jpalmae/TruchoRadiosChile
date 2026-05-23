package cl.truchoradios.chile.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.media3.common.util.BitmapLoader
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Downloads artwork images over HTTP and caches them locally.
 * Media3's default BitmapLoader only supports local files and content:// URIs.
 * This loader intercepts HTTP/HTTPS URIs, downloads the image, and returns the bitmap.
 */
class HttpBitmapLoader(private val context: Context) : BitmapLoader {

    private val cacheDir = File(context.cacheDir, "artwork").also { it.mkdirs() }

    override fun loadBitmap(uri: Uri): ListenableFuture<Bitmap> {
        return try {
            if (uri.scheme == "http" || uri.scheme == "https") {
                val bitmap = downloadAndCacheBitmap(uri.toString())
                Futures.immediateFuture(bitmap)
            } else {
                // Local/content URIs
                context.contentResolver.openInputStream(uri)?.use { input ->
                    val bitmap = BitmapFactory.decodeStream(input)
                    if (bitmap != null) {
                        Futures.immediateFuture(bitmap)
                    } else {
                        Futures.immediateFailedFuture(RuntimeException("Cannot decode: $uri"))
                    }
                } ?: Futures.immediateFailedFuture(RuntimeException("Cannot open: $uri"))
            }
        } catch (e: Exception) {
            Futures.immediateFailedFuture(e)
        }
    }

    override fun decodeBitmap(data: ByteArray): ListenableFuture<Bitmap> {
        return try {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            if (bitmap != null) {
                Futures.immediateFuture(bitmap)
            } else {
                Futures.immediateFailedFuture(RuntimeException("Cannot decode bitmap from byte[]"))
            }
        } catch (e: Exception) {
            Futures.immediateFailedFuture(e)
        }
    }

    override fun supportsMimeType(mimeType: String): Boolean {
        return mimeType.startsWith("image/")
    }

    private fun downloadAndCacheBitmap(urlStr: String): Bitmap {
        // Check cache first
        val cacheKey = Math.abs(urlStr.hashCode()).toString()
        val cacheFile = File(cacheDir, "$cacheKey.png")
        if (cacheFile.exists()) {
            val cached = BitmapFactory.decodeFile(cacheFile.absolutePath)
            if (cached != null) return cached
        }

        // Download
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 10000
        conn.readTimeout = 10000
        conn.setRequestProperty("User-Agent", "TruchoRadiosChile/1.0")
        conn.instanceFollowRedirects = true

        try {
            val bitmap = BitmapFactory.decodeStream(conn.inputStream)
                ?: throw RuntimeException("Failed to decode bitmap from $urlStr")

            // Save to cache
            try {
                FileOutputStream(cacheFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                }
            } catch (_: Exception) { }

            return bitmap
        } finally {
            conn.disconnect()
        }
    }
}
