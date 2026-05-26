package cl.truchoradios.chile.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.truchoradios.chile.player.PlaybackState
import cl.truchoradios.chile.player.RadioPlayerManager

private val RedAccent = Color(0xFFD52B1E)

@Composable
fun MiniPlayer(
    playerManager: RadioPlayerManager,
    onPlayerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentRadio by playerManager.currentRadio.collectAsState()
    val playbackState by playerManager.playbackState.collectAsState()
    val isPlaying by playerManager.isPlaying.collectAsState()

    AnimatedVisibility(
        visible = currentRadio != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        currentRadio?.let { radio ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    if (playbackState == PlaybackState.BUFFERING) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp),
                            color = RedAccent,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlayerClick() }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioImage(
                            imageUrl = radio.imageUrl,
                            name = radio.name,
                            size = 48.dp,
                            cornerRadius = 12.dp
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                radio.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                when (playbackState) {
                                    PlaybackState.BUFFERING -> "Buffering..."
                                    PlaybackState.PLAYING -> "En vivo"
                                    PlaybackState.PAUSED -> "Pausado"
                                    PlaybackState.ERROR -> "Error"
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = when (playbackState) {
                                    PlaybackState.BUFFERING -> MaterialTheme.colorScheme.tertiary
                                    PlaybackState.PLAYING -> Color(0xFF4CAF50)
                                    PlaybackState.PAUSED -> MaterialTheme.colorScheme.onSurfaceVariant
                                    PlaybackState.ERROR -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        if (playbackState == PlaybackState.BUFFERING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = RedAccent,
                                strokeWidth = 3.dp
                            )
                        } else {
                            FilledIconButton(
                                onClick = {
                                    if (playbackState == PlaybackState.PAUSED) {
                                        playerManager.resume()
                                    } else if (isPlaying) {
                                        playerManager.pause()
                                    } else {
                                        playerManager.play(radio)
                                    }
                                },
                                modifier = Modifier.size(40.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = RedAccent,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    if (isPlaying || playbackState == PlaybackState.PLAYING) Icons.Filled.Pause
                                    else Icons.Filled.PlayArrow,
                                    contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
