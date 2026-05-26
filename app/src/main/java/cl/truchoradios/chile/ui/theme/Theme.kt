package cl.truchoradios.chile.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import cl.truchoradios.chile.data.local.SettingsManager

private val TruchoRed = Color(0xFFD52B1E)
private val TruchoRedLight = Color(0xFFFF6B5E)

private val LightColorScheme = lightColorScheme(
    primary = TruchoRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF5A1210),
    secondary = Color(0xFF0039A6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE1FF),
    onSecondaryContainer = Color(0xFF001551),
    tertiary = Color(0xFF7D5700),
    onTertiary = Color.White,
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF3EFF1),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    error = Color(0xFFB3261E),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = TruchoRedLight,
    onPrimary = Color(0xFF410002),
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = Color(0xFFB4C5FF),
    onSecondary = Color(0xFF002A6E),
    secondaryContainer = Color(0xFF0D3F9B),
    onSecondaryContainer = Color(0xFFDCE1FF),
    tertiary = Color(0xFFFFB951),
    onTertiary = Color(0xFF422C00),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun TruchoRadiosTheme(
    settingsManager: SettingsManager? = null,
    content: @Composable () -> Unit,
) {
    val settingsDark by (settingsManager?.isDarkTheme?.collectAsState() ?: kotlinx.coroutines.flow.MutableStateFlow(false).collectAsState())
    val systemDark = isSystemInDarkTheme()
    val isDark = settingsDark || systemDark

    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
