package cl.truchoradios.chile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import cl.truchoradios.chile.R

@Composable
private fun TruchoPlaceholder(
    size: Dp,
    cornerRadius: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0xFF1B1B2F)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.trucho_logo),
            contentDescription = "Trucho Radios",
            modifier = Modifier.size(size),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun RadioImage(
    imageUrl: String,
    name: String,
    size: Dp = 120.dp,
    cornerRadius: Dp = 12.dp,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isBlank()) {
        TruchoPlaceholder(
            size = size,
            cornerRadius = cornerRadius,
            modifier = modifier
        )
    } else {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(cornerRadius)),
            contentScale = ContentScale.Crop,
            error = {
                TruchoPlaceholder(
                    size = size,
                    cornerRadius = cornerRadius
                )
            },
            loading = {
                TruchoPlaceholder(
                    size = size,
                    cornerRadius = cornerRadius
                )
            }
        )
    }
}
