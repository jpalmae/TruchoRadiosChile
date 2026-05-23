package cl.truchoradios.chile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TruchoRed = Color(0xFFD52B1E)
private val TruchoRedLight = Color(0xFFFF6B5E)

private val DarkColorScheme = darkColorScheme(
    primary = TruchoRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5A1210),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = Color(0xFFB6C4FF),
    onSecondary = Color.White,
    tertiary = Color(0xFFFFB74D),
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF444444),
    error = Color(0xFFCF6679),
    onError = Color.White,
)

@Composable
fun TruchoRadiosTheme(
    content: @Composable () -> Unit,
) {
    // Always dark theme
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content,
    )
}
