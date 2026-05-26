package cl.truchoradios.chile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.domain.model.StreamType

@Entity(tableName = "radios")
data class RadioEntity(
    @PrimaryKey val id: String,
    val name: String,
    val streamUrl: String,
    val streamType: String = StreamType.MP3.name,
    val imageUrl: String = "",
    val frequency: String = "",
    val city: String = "",
    val region: String = "",
    val genres: String = "",
    val description: String? = "",
    val website: String? = "",
    val listeners: Int = 0,
)

fun RadioEntity.toDomain(): Radio = Radio(
    id = id,
    name = name,
    streamUrl = streamUrl,
    streamType = StreamType.valueOf(streamType),
    imageUrl = imageUrl,
    frequency = frequency,
    city = city,
    region = region,
    genres = genres.split(",").filter { it.isNotBlank() },
    description = description ?: "",
    website = website ?: "",
    listeners = listeners,
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val radioId: String,
    val addedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "recent")
data class RecentEntity(
    @PrimaryKey val radioId: String,
    val playedAt: Long = System.currentTimeMillis(),
)
