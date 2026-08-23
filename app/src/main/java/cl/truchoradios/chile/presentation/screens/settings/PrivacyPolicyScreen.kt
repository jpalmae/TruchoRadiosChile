package cl.truchoradios.chile.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Política de privacidad") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Última actualización: 23 de agosto de 2026")
            PrivacySection(
                "Tu privacidad",
                "Trucho Radios Chile no solicita una cuenta. El desarrollador no recibe ni conserva nombres, correos electrónicos, ubicaciones precisas u otros datos de identificación personal, ni utiliza los datos para publicidad o seguimiento individual.",
            )
            PrivacySection(
                "Datos en el dispositivo",
                "Tus radios favoritas, el historial reciente y las preferencias permanecen exclusivamente en tu dispositivo y no se envían a servidores propios.",
            )
            PrivacySection(
                "Conexiones de terceros",
                "La app se conecta a transmisiones públicas de emisoras cuyos servidores pueden recibir datos técnicos habituales, como la dirección IP y el agente de usuario. Google Cast recopila automáticamente actividad anonimizada de Cast e información técnica del dispositivo, del SDK y de la app. Google cifra esos registros y los usa de forma agregada para analizar el uso, medir el rendimiento y detectar fallas. No contienen identificadores asociados a una persona ni se comparten con terceros, pero esta recopilación del SDK no se puede desactivar ni eliminar.",
            )
            PrivacySection(
                "Permisos",
                "Internet permite reproducir las radios. El servicio multimedia mantiene el audio en segundo plano. Las notificaciones muestran los controles de reproducción cuando las autorizas.",
            )
            PrivacySection(
                "Conservación y eliminación",
                "Como no mantenemos cuentas ni una base de datos de usuarios, no conservamos información personal en servidores propios. Puedes eliminar favoritos, historial y preferencias borrando los datos de la app o desinstalándola.",
            )
            PrivacySection(
                "Contacto",
                "Para consultas sobre privacidad: truchoradioschile@gmail.com",
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun PrivacySection(title: String, body: String) {
    Text(title, style = MaterialTheme.typography.titleMedium)
    Text(body, style = MaterialTheme.typography.bodyMedium)
}
