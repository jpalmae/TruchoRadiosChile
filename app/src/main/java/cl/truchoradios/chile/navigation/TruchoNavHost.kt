package cl.truchoradios.chile.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.truchoradios.chile.presentation.components.MiniPlayer
import cl.truchoradios.chile.presentation.components.TruchoBottomBar
import cl.truchoradios.chile.presentation.screens.favorites.FavoritesScreen
import cl.truchoradios.chile.presentation.screens.home.HomeScreen
import cl.truchoradios.chile.presentation.screens.player.FullPlayerScreen
import cl.truchoradios.chile.presentation.screens.radiolist.RadioListScreen
import cl.truchoradios.chile.presentation.screens.search.SearchScreen
import cl.truchoradios.chile.presentation.screens.settings.SettingsScreen
import cl.truchoradios.chile.player.RadioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val PLAYER = "player/{radioId}"
    const val RADIO_LIST = "radios/{filterType}/{filterValue}"

    fun player(radioId: String) = "player/$radioId"
    fun radioList(filterType: String, filterValue: String) = "radios/$filterType/$filterValue"
}

// ViewModel to share playerManager in the navigation graph
@HiltViewModel
class PlayerSharedViewModel @Inject constructor(
    val playerManager: RadioPlayerManager,
) : ViewModel()

@Composable
fun TruchoNavHost(
    navController: NavHostController = rememberNavController()
) {
    val sharedViewModel: PlayerSharedViewModel = hiltViewModel()
    val playerManager = sharedViewModel.playerManager
    val currentRadio by playerManager.currentRadio.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Screens that show bottom bar + mini player
    val showBottomBar = currentRoute in listOf(Routes.HOME, Routes.SEARCH, Routes.FAVORITES)
    val showMiniPlayer = currentRadio != null && currentRoute != Routes.PLAYER

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (showBottomBar) Modifier.padding(bottom = if (showMiniPlayer) 160.dp else 80.dp)
                    else Modifier
                )
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onRadioClick = { radioId ->
                        navController.navigate(Routes.player(radioId))
                    },
                    onSeeAllClick = { filterType, filterValue ->
                        navController.navigate(Routes.radioList(filterType, filterValue))
                    },
                    onSearchClick = { navController.navigate(Routes.SEARCH) },
                    onFavoritesClick = { navController.navigate(Routes.FAVORITES) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                )
            }

            composable(Routes.SEARCH) {
                SearchScreen(
                    onRadioClick = { radioId ->
                        navController.navigate(Routes.player(radioId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onRadioClick = { radioId ->
                        navController.navigate(Routes.player(radioId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.PLAYER,
                arguments = listOf(navArgument("radioId") { type = NavType.StringType })
            ) {
                FullPlayerScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.RADIO_LIST,
                arguments = listOf(
                    navArgument("filterType") { type = NavType.StringType },
                    navArgument("filterValue") { type = NavType.StringType },
                )
            ) {
                RadioListScreen(
                    onRadioClick = { radioId ->
                        navController.navigate(Routes.player(radioId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // Bottom bar + mini player overlay
        if (showBottomBar) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFF121212))
            ) {
                // Mini player
                AnimatedVisibility(
                    visible = showMiniPlayer,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    MiniPlayer(
                        playerManager = playerManager,
                        onPlayerClick = {
                            currentRadio?.let { radio ->
                                navController.navigate(Routes.player(radio.id))
                            }
                        }
                    )
                }

                // Bottom navigation
                TruchoBottomBar(
                    currentRoute = currentRoute ?: Routes.HOME,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Routes.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}
