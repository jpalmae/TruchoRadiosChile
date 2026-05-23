package cl.truchoradios.chile.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cl.truchoradios.chile.presentation.components.RadioImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerScreen(
    onBack: () -> Unit,
    viewModel: FullPlayerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val radio = uiState.radio

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
                Spacer(Modifier.height(24.dp))

                // Album art
                RadioImage(
                    imageUrl = radio.imageUrl,
                    name = radio.name,
                    size = 200.dp,
                    cornerRadius = 16.dp
                )

                Spacer(Modifier.height(32.dp))

                // Radio name
                Text(
                    text = radio.name,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                // Genre
                if (radio.genres.isNotEmpty()) {
                    Text(
                        text = radio.genres.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                // Frequency / City
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

                // Status
                Spacer(Modifier.height(12.dp))
                Text(
                    text = when {
                        uiState.isBuffering -> "Conectando..."
                        uiState.isPlaying -> if (uiState.rewindSeconds > 0)
                            "En vivo (-${uiState.rewindSeconds}s)"
                            else "En vivo"
                        uiState.error != null -> "Error de conexión"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = when {
                        uiState.isBuffering -> MaterialTheme.colorScheme.tertiary
                        uiState.isPlaying -> MaterialTheme.colorScheme.primary
                        uiState.error != null -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                // Rewind indicator
                if (uiState.rewindSeconds > 0 && uiState.isPlaying) {
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text(
                            text = "-${uiState.rewindSeconds}s",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Error detail
                uiState.error?.let { err ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.weight(1f))

                // Rewind controls
                if (uiState.canSeekBack) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RewindButton(seconds = 10, onClick = { viewModel.seekBack10() })
                        Spacer(Modifier.size(16.dp))
                        RewindButton(seconds = 30, onClick = { viewModel.seekBack30() })
                        Spacer(Modifier.size(16.dp))
                        RewindButton(seconds = 60, onClick = { viewModel.seekBack60() })

                        if (uiState.rewindSeconds > 0) {
                            Spacer(Modifier.size(16.dp))
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clickable { viewModel.seekToLive() }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.Update,
                                        contentDescription = "Volver en vivo",
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.White
                                    )
                                    Text(
                                        "En vivo",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Main controls row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Favorite
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

                    // Play / Pause
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

                    // Share
                    IconButton(onClick = { /* TODO: Share */ }) {
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

@Composable
private fun RewindButton(seconds: Int, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                Icons.Default.Replay10,
                contentDescription = "Retroceder ${seconds}s",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Show the seconds as a small badge
            Text(
                text = "$seconds",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
            )
        }
    }
}
