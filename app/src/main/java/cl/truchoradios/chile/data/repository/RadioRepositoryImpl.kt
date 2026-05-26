package cl.truchoradios.chile.data.repository

import android.content.Context
import cl.truchoradios.chile.data.local.dao.FavoriteDao
import cl.truchoradios.chile.data.local.dao.RadioDao
import cl.truchoradios.chile.data.local.dao.RecentDao
import cl.truchoradios.chile.data.local.entity.FavoriteEntity
import cl.truchoradios.chile.data.local.entity.RadioEntity
import cl.truchoradios.chile.data.local.entity.RecentEntity
import cl.truchoradios.chile.domain.model.Genre
import cl.truchoradios.chile.domain.model.Region
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

data class RadiosJson(
    val radios: List<RadioEntityJson> = emptyList(),
    val regions: List<RegionJson> = emptyList(),
    val genres: List<GenreJson> = emptyList(),
)

data class RadioEntityJson(
    val id: String, val name: String, val streamUrl: String,
    val streamType: String = "MP3", val imageUrl: String = "",
    val frequency: String = "", val city: String = "",
    val region: String = "", val genres: String = "",
    val description: String? = "", val website: String? = "",
    val listeners: Int = 0,
)

data class RegionJson(val id: String, val name: String, val radioCount: Int = 0)
data class GenreJson(val id: String, val name: String, val icon: String? = "", val radioCount: Int = 0)

@Singleton
class RadioRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val radioDao: RadioDao,
    private val favoriteDao: FavoriteDao,
    private val recentDao: RecentDao,
) {
    private val gson = Gson()
    @Volatile
    private var cachedData: RadiosJson? = null

    private fun loadData(): RadiosJson {
        cachedData?.let { return it }
        return synchronized(this) {
            cachedData ?: run {
                val json = context.assets.open("radios.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<RadiosJson>() {}.type
                val data: RadiosJson = gson.fromJson(json, type)
                cachedData = data
                data
            }
        }
    }

    fun getAllRadios(): Flow<List<RadioEntity>> = radioDao.getAllRadios()
    suspend fun getRadioById(id: String): RadioEntity? = radioDao.getRadioById(id)

    fun getRegions(): List<Region> = loadData().regions.map {
        Region(id = it.id, name = it.name, radioCount = it.radioCount)
    }

    fun getGenres(): List<Genre> = loadData().genres.map {
        Genre(id = it.id, name = it.name, icon = it.icon ?: "", radioCount = it.radioCount)
    }

    suspend fun loadRadiosIfEmpty() {
        if (radioDao.count() > 0) return
        val data = loadData()
        radioDao.insertAll(data.radios.map { r ->
            RadioEntity(
                id = r.id, name = r.name, streamUrl = r.streamUrl,
                streamType = r.streamType, imageUrl = r.imageUrl,
                frequency = r.frequency, city = r.city, region = r.region,
                genres = r.genres, description = r.description ?: "",
                website = r.website ?: "", listeners = r.listeners,
            )
        })
    }

    fun searchRadios(query: String): Flow<List<RadioEntity>> = radioDao.searchRadios(query)
    fun getRadiosByRegion(region: String): Flow<List<RadioEntity>> = radioDao.getRadiosByRegion(region)
    fun getRadiosByGenre(genre: String): Flow<List<RadioEntity>> = radioDao.getRadiosByGenre(genre)

    suspend fun addFavorite(radioId: String) = favoriteDao.addFavorite(FavoriteEntity(radioId))
    suspend fun removeFavorite(radioId: String) = favoriteDao.removeFavorite(FavoriteEntity(radioId))
    fun isFavorite(radioId: String): Flow<Boolean> = favoriteDao.isFavorite(radioId)
    fun getFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getFavorites()

    suspend fun addRecent(radioId: String) = recentDao.addRecent(RecentEntity(radioId))
    fun getRecent(): Flow<List<RecentEntity>> = recentDao.getRecent()
}
