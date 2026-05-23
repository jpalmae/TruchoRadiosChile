package cl.truchoradios.chile.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.presentation.components.RadioImage
import cl.truchoradios.chile.domain.model.Genre
import cl.truchoradios.chile.domain.model.Region

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onRadioClick: (String) -> Unit,
    onSeeAllClick: (String, String) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFFD52B1E)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 160.dp) // space for mini player + bottom bar
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "TRUCHO",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFD52B1E),
                        letterSpacing = 2.sp
                    )
                    Row {
                        IconButton(onClick = onSearchClick) {
                            Icon(Icons.Default.Search, "Buscar", tint = Color.White)
                        }
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Default.Settings, "Ajustes", tint = Color(0xFF808080))
                        }
                    }
                }

                // Radios Populares - Horizontal scroll
                SectionHeader("🔥 Populares") {
                    onSeeAllClick("popular", "all")
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.popularRadios) { radio ->
                        PopularRadioCard(radio = radio, onClick = { onRadioClick(radio.id) })
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Regiones - Chips
                SectionHeader("📍 Regiones")
                FlowRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.regions.filter { it.radioCount > 0 }.forEach { region ->
                        DarkChip(text = "${region.name} (${region.radioCount})") {
                            onSeeAllClick("region", region.id)
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Géneros - Chips
                SectionHeader("🎵 Géneros")
                FlowRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.genres.forEach { genre ->
                        DarkChip(text = "${genre.icon ?: "📻"} ${genre.name}") {
                            onSeeAllClick("genre", genre.id)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        onSeeAll?.let {
            TextButton(onClick = it) {
                Text("Ver todo", color = Color(0xFFD52B1E), fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PopularRadioCard(radio: Radio, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RadioImage(
                imageUrl = radio.imageUrl,
                name = radio.name,
                size = 130.dp,
                cornerRadius = 16.dp
            )
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                Text(
                    radio.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (radio.frequency.isNotBlank()) {
                    Text(
                        radio.frequency,
                        fontSize = 10.sp,
                        color = Color(0xFF808080)
                    )
                }
            }
        }
    }
}

@Composable
private fun DarkChip(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF2A2A2A),
        contentColor = Color.White
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 13.sp,
            color = Color.White
        )
    }
}
