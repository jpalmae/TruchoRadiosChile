package cl.truchoradios.chile.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.truchoradios.chile.data.local.entity.RadioEntity
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.domain.model.Region
import cl.truchoradios.chile.domain.model.Genre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val popularRadios: List<Radio> = emptyList(),
    val recentRadios: List<Radio> = emptyList(),
    val regions: List<Region> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RadioRepositoryImpl,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = repository.getAllRadios()
        .map { entities ->
            val radios = entities.map { it.toDomain() }
            HomeUiState(
                popularRadios = radios.sortedByDescending { it.listeners }.take(15),
                regions = repository.getRegions(),
                genres = repository.getGenres(),
                isLoading = false,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        viewModelScope.launch {
            repository.loadRadiosIfEmpty()
        }
    }

    private fun RadioEntity.toDomain() = Radio(
        id = id, name = name, streamUrl = streamUrl,
        streamType = cl.truchoradios.chile.domain.model.StreamType.valueOf(streamType),
        imageUrl = imageUrl, frequency = frequency, city = city,
        region = region, genres = genres.split(",").filter { it.isNotBlank() },
        description = description ?: "", website = website ?: "", listeners = listeners,
    )
}
