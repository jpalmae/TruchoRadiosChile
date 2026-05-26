package cl.truchoradios.chile.presentation.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.truchoradios.chile.data.local.entity.toDomain
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.player.RadioPlayerManager
import cl.truchoradios.chile.presentation.components.RadioImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class FavoritesUiState(val radios: List<Radio> = emptyList())

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RadioRepositoryImpl,
    val playerManager: RadioPlayerManager,
) : ViewModel() {
    val uiState: StateFlow<FavoritesUiState> = repository.getFavorites()
        .flatMapLatest { favs ->
            flow {
                val radios = favs.mapNotNull { fav ->
                    repository.getRadioById(fav.radioId)?.toDomain()
                }
                emit(FavoritesUiState(radios))
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoritesUiState())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onRadioClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRadio by viewModel.playerManager.currentRadio.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (uiState.radios.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "No tienes favoritos aún",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(uiState.radios, key = { it.id }) { radio ->
                    RadioListItem(
                        radio = radio,
                        isPlaying = currentRadio?.id == radio.id,
                        onClick = { onRadioClick(radio.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RadioListItem(
    radio: Radio,
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = borderColor,
        tonalElevation = if (isPlaying) 4.dp else 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioImage(
                imageUrl = radio.imageUrl,
                name = radio.name,
                size = 56.dp,
                cornerRadius = 12.dp,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = radio.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (isPlaying) {
                        Spacer(Modifier.width(6.dp))
                        Text("\u25CF", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text(
                    text = listOf(radio.genres.firstOrNull(), radio.frequency)
                        .filter { !it.isNullOrBlank() }
                        .joinToString(" · "),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
