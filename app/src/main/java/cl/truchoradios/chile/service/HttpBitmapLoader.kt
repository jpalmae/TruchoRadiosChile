package cl.truchoradios.chile.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.media3.common.util.BitmapLoader
import androidx.media3.common.util.UnstableApi
import cl.truchoradios.chile.R
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/**
 * Downloads artwork images over HTTP and caches them locally.
 * Media3's default BitmapLoader only supports local files and content:// URIs.
 * This loader intercepts HTTP/HTTPS URIs, downloads the image, and returns the bitmap.
 * Las descargas se hacen en segundo plano: loadBitmap es invocado en el hilo
 * principal de la sesion y bloquearlo congela toda la app (p.ej. Android Auto).
 */
@UnstableApi
class HttpBitmapLoader(private val context: Context) : BitmapLoader {

    private val cacheDir = File(context.cacheDir, "artwork").also { it.mkdirs() }
    private val executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool())

    override fun loadBitmap(uri: Uri): ListenableFuture<Bitmap> =
        executor.submit<Bitmap> {
            try {
                val bitmap = when {
                    uri.scheme == "http" || uri.scheme == "https" -> {
                        downloadAndCacheBitmap(uri.toString())
                    }
                    uri.scheme == "file" && uri.path?.startsWith(ASSET_PATH_PREFIX) == true -> {
                        loadAssetBitmap(uri)
                    }
                    else -> {
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            BitmapFactory.decodeStream(input)
                        } ?: throw RuntimeException("Cannot open: $uri")
                    }
                }
                scaleDown(bitmap)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load artwork $uri; using fallback", e)
                fallbackBitmap()
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

    private fun loadAssetBitmap(uri: Uri): Bitmap {
        val assetPath = Uri.decode(uri.path.orEmpty().removePrefix(ASSET_PATH_PREFIX))
        require(assetPath.isNotBlank() && ".." !in assetPath) { "Invalid asset path: $uri" }
        val bitmapPath = if (assetPath.endsWith(".svg", ignoreCase = true)) {
            assetPath.substringBeforeLast('.') + ".png"
        } else {
            assetPath
        }
        return context.assets.open(bitmapPath).use { input ->
            BitmapFactory.decodeStream(input)
                ?: throw RuntimeException("Cannot decode asset: $bitmapPath")
        }
    }

    private fun fallbackBitmap(): Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.trucho_logo)
            ?: throw RuntimeException("Cannot decode fallback artwork")

    private fun scaleDown(bitmap: Bitmap): Bitmap {
        val longestSide = maxOf(bitmap.width, bitmap.height)
        if (longestSide <= MAX_ARTWORK_SIZE_PX) return bitmap

        val scale = MAX_ARTWORK_SIZE_PX.toFloat() / longestSide
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * scale).toInt().coerceAtLeast(1),
            (bitmap.height * scale).toInt().coerceAtLeast(1),
            true
        )
    }

    private companion object {
        const val TAG = "HttpBitmapLoader"
        const val ASSET_PATH_PREFIX = "/android_asset/"
        const val MAX_ARTWORK_SIZE_PX = 512
    }
}
