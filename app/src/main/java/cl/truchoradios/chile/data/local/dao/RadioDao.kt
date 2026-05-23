package cl.truchoradios.chile.data.local.dao

import androidx.room.*
import cl.truchoradios.chile.data.local.entity.RadioEntity
import cl.truchoradios.chile.data.local.entity.FavoriteEntity
import cl.truchoradios.chile.data.local.entity.RecentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioDao {
    @Query("SELECT * FROM radios ORDER BY listeners DESC")
    fun getAllRadios(): Flow<List<RadioEntity>>

    @Query("SELECT * FROM radios WHERE id = :id")
    suspend fun getRadioById(id: String): RadioEntity?

    @Query("SELECT * FROM radios WHERE name LIKE '%' || :query || '%'")
    fun searchRadios(query: String): Flow<List<RadioEntity>>

    @Query("SELECT * FROM radios WHERE region = :region ORDER BY listeners DESC")
    fun getRadiosByRegion(region: String): Flow<List<RadioEntity>>

    @Query("SELECT * FROM radios WHERE genres LIKE '%' || :genre || '%' ORDER BY listeners DESC")
    fun getRadiosByGenre(genre: String): Flow<List<RadioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(radios: List<RadioEntity>)

    @Query("SELECT COUNT(*) FROM radios")
    suspend fun count(): Int
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE radioId = :radioId)")
    fun isFavorite(radioId: String): Flow<Boolean>
}

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent ORDER BY playedAt DESC LIMIT 20")
    fun getRecent(): Flow<List<RecentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecent(recent: RecentEntity)

    @Query("DELETE FROM recent WHERE radioId = :radioId")
    suspend fun removeRecent(radioId: String)
}
