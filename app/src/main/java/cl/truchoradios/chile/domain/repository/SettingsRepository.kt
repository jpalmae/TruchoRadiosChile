package cl.truchoradios.chile.domain.repository

/**
 * Repository para preferencias y configuración de la aplicación.
 */
interface SettingsRepository {
    val isDarkTheme: kotlinx.coroutines.flow.StateFlow<Boolean>
    val volume: kotlinx.coroutines.flow.StateFlow<Float>
    
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun toggleDarkTheme()
    suspend fun setVolume(level: Float)
}
