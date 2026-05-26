package cl.truchoradios.chile.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Radio(
    val id: String,
    val name: String,
    val streamUrl: String,
    val streamType: StreamType = StreamType.MP3,
    val imageUrl: String = "",
    val frequency: String = "",
    val city: String = "",
    val region: String = "",
    val genres: List<String> = emptyList(),
    val description: String = "",
    val website: String = "",
    val isFavorite: Boolean = false,
    val listeners: Int = 0,
)

enum class StreamType { MP3, AAC, HLS, OGG }

@Stable
data class Region(
    val id: String,
    val name: String,
    val radioCount: Int = 0,
)

@Stable
data class Genre(
    val id: String,
    val name: String,
    val icon: String? = "",
    val radioCount: Int = 0,
)
