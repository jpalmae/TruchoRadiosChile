package cl.truchoradios.chile.presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.sin

/**
 * Animated spectrum visualizer that responds to playback state.
 * Uses infinite animation to drive real-time bar movement.
 * No permissions required — works on all devices and emulators.
 */
@Composable
fun SpectrumVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
) {
    // Infinite animation that continuously increments — drives all movement
    val infiniteTransition = rememberInfiniteTransition(label = "spectrum")
    val animValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000)
        ),
        label = "spectrumAnim"
    )

    // Second animation offset for more complex motion
    val animValue2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 45000)
        ),
        label = "spectrumAnim2"
    )

    val barCount = 48

    // Remember bar phases for variety
    val barPhases = remember { FloatArray(barCount) { kotlin.random.Random.nextFloat() * 6.28f } }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val totalBarWidth = size.width / barCount
        val barWidth = totalBarWidth * 0.65f
        val gap = totalBarWidth * 0.35f
        val cornerRadius = barWidth / 2f

        for (i in 0 until barCount) {
            val value: Float
            if (isPlaying) {
                // Layered sine waves simulating frequency spectrum
                val t = animValue * 0.05f
                val t2 = animValue2 * 0.05f
                val baseFreq = sin(t * 2.5f + i * 0.35f) * 0.3f + 0.4f
                val midFreq = sin(t2 * 4.1f + i * 0.55f + barPhases[i]) * 0.25f
                val highFreq = sin(t * 7.3f + i * 0.8f) * 0.15f
                // Use animValue for pseudo-random noise (deterministic but looks random)
                val noise = sin(t * 13.7f + i * 2.3f + barPhases[i] * 3f) * 0.1f

                // Bass boost on left bars
                val bassBoost = if (i < barCount * 0.25f) 0.15f else 0f
                val midCut = if (i > barCount * 0.4f && i < barCount * 0.6f) -0.08f else 0f

                value = (baseFreq + midFreq + highFreq + noise + bassBoost + midCut)
                    .coerceIn(0.08f, 1f)
            } else {
                // Gentle idle breathing
                val t = animValue * 0.02f
                value = (sin(t + i * 0.3f) * 0.04f + 0.06f).coerceIn(0.03f, 0.12f)
            }

            val barHeight = (value * size.height * 0.9f).coerceAtLeast(3f)
            val x = i * totalBarWidth + gap / 2f
            val y = size.height - barHeight

            // Color gradient: Chile red → orange → yellow
            val ratio = i.toFloat() / barCount
            val color = when {
                ratio < 0.33f -> Color(0xFFD52B1E)
                ratio < 0.66f -> Color(0xFFFF6F3C)
                else -> Color(0xFFFFC93C)
            }
            val alpha = (0.7f + value * 0.3f).coerceIn(0.7f, 1f)

            drawRoundRect(
                color = color.copy(alpha = alpha),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            )
        }
    }
}
