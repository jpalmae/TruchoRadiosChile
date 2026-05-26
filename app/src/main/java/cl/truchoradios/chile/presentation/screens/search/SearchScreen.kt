package cl.truchoradios.chile.presentation.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(val query: String = "", val results: List<Radio> = emptyList())

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RadioRepositoryImpl,
    val playerManager: RadioPlayerManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        viewModelScope.launch {
            if (query.length >= 2) {
                repository.searchRadios(query).collect { entities ->
                    _uiState.value = _uiState.value.copy(
                        results = entities.map { it.toDomain() }
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(results = emptyList())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRadioClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRadio by viewModel.playerManager.currentRadio.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.onQueryChange(it) },
                placeholder = { Text("Buscar radios\u2026") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
            )

            if (uiState.results.isEmpty() && uiState.query.length >= 2) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "No se encontraron radios",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(uiState.results, key = { it.id }) { radio ->
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
