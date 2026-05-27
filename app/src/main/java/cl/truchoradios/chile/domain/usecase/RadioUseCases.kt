package cl.truchoradios.chile.domain.usecase

import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.domain.repository.RadioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todas las radios.
 */
class GetAllRadios @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(): Flow<List<Radio>> = repository.getAllRadios()
}

/**
 * Caso de uso para obtener una radio por ID.
 */
class GetRadioById @Inject constructor(
    private val repository: RadioRepository
) {
    suspend operator fun invoke(id: String): Radio? = repository.getRadioById(id)
}

/**
 * Caso de uso para buscar radios.
 */
class SearchRadios @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(query: String): Flow<List<Radio>> = repository.searchRadios(query)
}

/**
 * Caso de uso para obtener radios por región.
 */
class GetRadiosByRegion @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(region: String): Flow<List<Radio>> = repository.getRadiosByRegion(region)
}

/**
 * Caso de uso para obtener radios por género.
 */
class GetRadiosByGenre @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(genre: String): Flow<List<Radio>> = repository.getRadiosByGenre(genre)
}

/**
 * Caso de uso para agregar a favoritos.
 */
class AddFavorite @Inject constructor(
    private val repository: RadioRepository
) {
    suspend operator fun invoke(radioId: String) = repository.addFavorite(radioId)
}

/**
 * Caso de uso para remover de favoritos.
 */
class RemoveFavorite @Inject constructor(
    private val repository: RadioRepository
) {
    suspend operator fun invoke(radioId: String) = repository.removeFavorite(radioId)
}

/**
 * Caso de uso para verificar si es favorito.
 */
class IsFavorite @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(radioId: String): Flow<Boolean> = repository.isFavorite(radioId)
}

/**
 * Caso de uso para obtener favoritos.
 */
class GetFavorites @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(): Flow<List<Radio>> = repository.getFavorites()
}

/**
 * Caso de uso para agregar a recientes.
 */
class AddRecent @Inject constructor(
    private val repository: RadioRepository
) {
    suspend operator fun invoke(radioId: String) = repository.addRecent(radioId)
}

/**
 * Caso de uso para obtener recientes.
 */
class GetRecent @Inject constructor(
    private val repository: RadioRepository
) {
    operator fun invoke(): Flow<List<Radio>> = repository.getRecent()
}
