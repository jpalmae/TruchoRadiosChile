package cl.truchoradios.chile.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.truchoradios.chile.data.local.SettingsManager
import cl.truchoradios.chile.player.RadioPlayerManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsManager: SettingsManager? = null,
    playerManager: RadioPlayerManager? = null,
) {
    val isDark by (settingsManager?.isDarkTheme?.collectAsState() ?: remember { mutableStateOf(false) })
    val sleepRemaining by (playerManager?.sleepTimerRemaining?.collectAsState() ?: remember { mutableLongStateOf(0L) })
    var showSleepDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Trucho Radios Chile", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("v1.0.0", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Reproductor de radios chilenas \uD83C\uDDE8\uD83C\uDDF1\nTodas tus emisoras favoritas en un solo lugar.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Apariencia", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                if (isDark) "Modo oscuro" else "Modo claro",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Switch(
                            checked = isDark,
                            onCheckedChange = { settingsManager?.toggleDarkTheme() }
                        )
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sleep Timer", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    if (sleepRemaining > 0) {
                        val minutes = sleepRemaining / 60_000
                        Text(
                            "Apagando en ${minutes}min ${(sleepRemaining % 60_000) / 1000}s",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(4.dp))
                        TextButton(onClick = { playerManager?.cancelSleepTimer() }) {
                            Text("Cancelar sleep timer")
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.width(12.dp))
                                Text("Programar apagado", style = MaterialTheme.typography.bodyMedium)
                            }
                            TextButton(onClick = { showSleepDialog = true }) {
                                Text("Configurar")
                            }
                        }
                    }
                }
            }
        }

        if (showSleepDialog) {
            val options = listOf(15, 30, 45, 60, 90, 120)
            AlertDialog(
                onDismissRequest = { showSleepDialog = false },
                title = { Text("Sleep Timer") },
                text = {
                    Column {
                        Text("La radio se apagará automáticamente después de:")
                        Spacer(Modifier.height(8.dp))
                        options.forEach { mins ->
                            TextButton(
                                onClick = {
                                    playerManager?.scheduleSleepTimer(mins)
                                    showSleepDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (mins < 60) "${mins} minutos" else "${mins / 60} hora${if (mins > 60) "s" else ""}",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSleepDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
