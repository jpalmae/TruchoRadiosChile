package cl.truchoradios.chile.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        radio?.let {
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(80.dp))

                // Logo grande
                RadioImage(
                    imageUrl = radio.imageUrl,
                    name = radio.name,
                    size = 240.dp,
                    cornerRadius = 24.dp
                )

                Spacer(Modifier.height(32.dp))

                // Nombre
                Text(
                    radio.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                // Frecuencia / Ciudad
                if (radio.frequency.isNotBlank() || radio.city.isNotBlank()) {
                    Text(
                        listOf(radio.frequency, radio.city).filter { it.isNotBlank() }.joinToString(" · "),
                        fontSize = 15.sp,
                        color = Color(0xFFB0B0B0)
                    )
                }

                // Géneros
                if (radio.genres.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        radio.genres.joinToString(", "),
                        fontSize = 13.sp,
                        color = Color(0xFFD52B1E)
                    )
                }

                // Status
                Spacer(Modifier.height(8.dp))
                Text(
                    when {
                        uiState.isBuffering -> "⏳ Conectando..."
                        uiState.isPlaying -> "● En vivo"
                        uiState.error != null -> "⚠ Error de conexión"
                        else -> ""
                    },
                    fontSize = 13.sp,
                    color = when {
                        uiState.isBuffering -> Color(0xFFFFB74D)
                        uiState.isPlaying -> Color(0xFF4CAF50)
                        uiState.error != null -> Color(0xFFE53935)
                        else -> Color(0xFF808080)
                    }
                )

                // Error detail
                uiState.error?.let { err ->
                    Spacer(Modifier.height(4.dp))
                    Text(err, fontSize = 11.sp, color = Color(0xFF808080))
                }

                Spacer(Modifier.weight(1f))

                // Controls
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
                            if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            "Favorito",
                            tint = if (uiState.isFavorite) Color(0xFFD52B1E) else Color(0xFF808080),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Play/Pause - big circle
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isBuffering) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD52B1E)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = Color.White,
                                    strokeWidth = 3.dp
                                )
                            }
                        } else {
                            FloatingActionButton(
                                onClick = { if (uiState.isPlaying) viewModel.stop() else viewModel.play() },
                                modifier = Modifier.size(80.dp),
                                containerColor = Color(0xFFD52B1E),
                                contentColor = Color.White
                            ) {
                                Icon(
                                    if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    if (uiState.isPlaying) "Pausar" else "Reproducir",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }

                    // Share
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(
                            Icons.Default.Share,
                            "Compartir",
                            tint = Color(0xFF808080),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        } ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFD52B1E))
            }
        }
    }
}
