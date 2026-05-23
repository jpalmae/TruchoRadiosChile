package cl.truchoradios.chile.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.player.PlaybackState
import cl.truchoradios.chile.player.RadioPlayerManager

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
            Column {
                // Loading/buffering indicator
                if (playbackState == PlaybackState.BUFFERING) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = Color(0xFFD52B1E),
                        trackColor = Color(0xFF333333),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF252535),
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .clickable { onPlayerClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Radio image
                    RadioImage(
                        imageUrl = radio.imageUrl,
                        name = radio.name,
                        size = 48.dp,
                        cornerRadius = 12.dp
                    )

                    Spacer(Modifier.width(12.dp))

                    // Radio info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            radio.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            when (playbackState) {
                                PlaybackState.BUFFERING -> "Buffering..."
                                PlaybackState.PLAYING -> "En vivo"
                                PlaybackState.ERROR -> "Error"
                                else -> ""
                            },
                            fontSize = 11.sp,
                            color = when (playbackState) {
                                PlaybackState.BUFFERING -> Color(0xFFFFB74D)
                                PlaybackState.PLAYING -> Color(0xFF4CAF50)
                                PlaybackState.ERROR -> Color(0xFFE53935)
                                else -> Color(0xFF808080)
                            }
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    // Play/Pause button
                    if (playbackState == PlaybackState.BUFFERING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Color(0xFFD52B1E),
                            strokeWidth = 3.dp
                        )
                    } else {
                        IconButton(
                            onClick = {
                                if (isPlaying) {
                                    playerManager.stop()
                                } else {
                                    playerManager.play(radio)
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFD52B1E), CircleShape)
                        ) {
                            Icon(
                                if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
