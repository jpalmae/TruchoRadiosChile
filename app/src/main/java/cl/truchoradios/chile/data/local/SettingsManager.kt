package cl.truchoradios.chile.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "trucho_settings", 
        Context.MODE_PRIVATE
    )
    
    private val mutex = Mutex()
    
    private val _isDarkTheme = MutableStateFlow(
        prefs.getBoolean("dark_theme", false)
    )
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme
    
    private val _volume = MutableStateFlow(
        prefs.getFloat("volume", 1.0f)
    )
    val volume: StateFlow<Float> = _volume
    
    suspend fun toggleDarkTheme() = mutex.withLock {
        val newValue = !_isDarkTheme.value
        prefs.edit().putBoolean("dark_theme", newValue).apply()
        _isDarkTheme.value = newValue
    }
    
    suspend fun setDarkTheme(enabled: Boolean) = mutex.withLock {
        prefs.edit().putBoolean("dark_theme", enabled).apply()
        _isDarkTheme.value = enabled
    }
    
    suspend fun setVolume(level: Float) = mutex.withLock {
        val normalized = level.coerceIn(0f, 1f)
        prefs.edit().putFloat("volume", normalized).apply()
        _volume.value = normalized
    }
}
