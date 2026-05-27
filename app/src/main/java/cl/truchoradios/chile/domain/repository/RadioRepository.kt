package cl.truchoradios.chile.domain.repository

import cl.truchoradios.chile.domain.model.Radio
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface para operaciones de radios.
 * Define el contrato que debe implementar la capa de datos.
 */
interface RadioRepository {
    fun getAllRadios(): Flow<List<Radio>>
    suspend fun getRadioById(id: String): Radio?
    fun searchRadios(query: String): Flow<List<Radio>>
    fun getRadiosByRegion(region: String): Flow<List<Radio>>
    fun getRadiosByGenre(genre: String): Flow<List<Radio>>
    
    // Favoritos
    suspend fun addFavorite(radioId: String)
    suspend fun removeFavorite(radioId: String)
    fun isFavorite(radioId: String): Flow<Boolean>
    fun getFavorites(): Flow<List<Radio>>
    
    // Recientes
    suspend fun addRecent(radioId: String)
    fun getRecent(): Flow<List<Radio>>
}
