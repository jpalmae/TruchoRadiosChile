package cl.truchoradios.chile.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector

private val RedAccent = Color(0xFFD52B1E)

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val NAV_ITEMS = listOf(
    BottomNavItem("home", "Inicio", Icons.Filled.Home),
    BottomNavItem("search", "Buscar", Icons.Filled.Search),
    BottomNavItem("favorites", "Favoritos", Icons.Filled.Favorite),
)

@Composable
fun TruchoBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 3.dp,
    ) {
        NAV_ITEMS.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(item.label)
                },
                selected = selected,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RedAccent,
                    selectedTextColor = RedAccent,
                    unselectedIconColor = Color(0xFF808080),
                    unselectedTextColor = Color(0xFF808080),
                    indicatorColor = RedAccent.copy(alpha = 0.12f),
                )
            )
        }
    }
}
