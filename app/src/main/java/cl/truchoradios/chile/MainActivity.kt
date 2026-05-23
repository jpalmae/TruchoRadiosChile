package cl.truchoradios.chile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cl.truchoradios.chile.navigation.TruchoNavHost
import cl.truchoradios.chile.player.RadioPlayerManager
import cl.truchoradios.chile.ui.theme.TruchoRadiosTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var playerManager: RadioPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen before super.onCreate
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep splash visible until Compose renders first frame
        splashScreen.setKeepOnScreenCondition { false }

        setContent {
            TruchoRadiosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TruchoNavHost()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop playback and release player when app is closed
        if (isFinishing) {
            playerManager.stop()
            playerManager.player.release()
        }
    }
}
