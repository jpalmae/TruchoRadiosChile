package cl.truchoradios.chile.media

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import cl.truchoradios.chile.R

fun Context.resolveArtworkUri(imageUrl: String?): Uri {
    val imageUri = imageUrl
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?.toUri()

    return imageUri
        ?.takeIf { it.scheme in SUPPORTED_ARTWORK_SCHEMES }
        ?: "android.resource://$packageName/${R.drawable.trucho_logo}".toUri()
}

private val SUPPORTED_ARTWORK_SCHEMES = setOf(
    "android.resource",
    "content",
    "file",
    "http",
    "https",
)
