package cl.truchoradios.chile.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays the frequency bands measured from ExoPlayer's decoded audio stream.
 */
@Composable
fun SpectrumVisualizer(
    bands: List<Float>,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
) {
    val activity by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 280),
        label = "spectrumActivity",
    )
    val barCount = 48

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
            val measuredValue = bands.getOrElse(i) { 0f }.coerceIn(0f, 1f)
            val value = 0.025f + measuredValue * activity * 0.975f

            val barHeight = (value * size.height * 0.9f).coerceAtLeast(3f)
            val x = i * totalBarWidth + gap / 2f
            val y = size.height - barHeight

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
