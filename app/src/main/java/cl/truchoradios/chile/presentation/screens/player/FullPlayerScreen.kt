package cl.truchoradios.chile.presentation.screens.player

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cl.truchoradios.chile.player.RadioPlayerManager
import cl.truchoradios.chile.presentation.components.RadioImage
import cl.truchoradios.chile.presentation.components.SpectrumVisualizer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerScreen(
    onBack: () -> Unit,
    playerManager: RadioPlayerManager,
    viewModel: FullPlayerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val radio = uiState.radio
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ahora reproduciendo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        radio?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                RadioImage(
                    imageUrl = radio.imageUrl,
                    name = radio.name,
                    size = 180.dp,
                    cornerRadius = 16.dp
                )

                Spacer(Modifier.height(16.dp))

                SpectrumVisualizer(
                    isPlaying = uiState.isPlaying,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = radio.name,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                if (radio.genres.isNotEmpty()) {
                    Text(
                        text = radio.genres.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                if (radio.frequency.isNotBlank() || radio.city.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = listOf(radio.frequency, radio.city)
                            .filter { it.isNotBlank() }
                            .joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = when {
                        uiState.isBuffering -> "Conectando..."
                        uiState.isPlaying -> "En vivo"
                        uiState.error != null -> "Error de conexión"
                        else -> "Pausado"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = when {
                        uiState.isBuffering -> MaterialTheme.colorScheme.tertiary
                        uiState.isPlaying -> MaterialTheme.colorScheme.primary
                        uiState.error != null -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                uiState.error?.let { err ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Volume slider
                var volume by rememberSaveable { mutableFloatStateOf(playerManager.player.volume) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Volumen",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Slider(
                        value = volume,
                        onValueChange = { vol ->
                            volume = vol
                            playerManager.setVolume(vol)
                        },
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }

                Spacer(Modifier.weight(1f))

                // Main controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (uiState.isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (uiState.isFavorite) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isBuffering) {
                            FloatingActionButton(
                                onClick = {},
                                modifier = Modifier.size(72.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        } else {
                            FloatingActionButton(
                                onClick = {
                                    if (uiState.isPlaying) viewModel.stop() else viewModel.play()
                                },
                                modifier = Modifier.size(72.dp)
                            ) {
                                Icon(
                                    imageVector = if (uiState.isPlaying) Icons.Default.Pause
                                    else Icons.Default.PlayArrow,
                                    contentDescription = if (uiState.isPlaying) "Pausar"
                                    else "Reproducir",
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Escuchando ${radio.name} en Trucho Radios Chile! \uD83C\uDDE8\uD83C\uDDF1 \uD83D\uDCFB")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Compartir"))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
