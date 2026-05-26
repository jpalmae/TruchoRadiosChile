package cl.truchoradios.chile.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.truchoradios.chile.data.local.entity.toDomain
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.domain.model.Region
import cl.truchoradios.chile.domain.model.Genre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
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

    private val cachedRegions: List<Region> = repository.getRegions()
    private val cachedGenres: List<Genre> = repository.getGenres()

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllRadios(),
        repository.getRecent(),
    ) { allEntities, recents ->
        val radios = allEntities.map { it.toDomain() }
        HomeUiState(
            popularRadios = radios.sortedByDescending { it.listeners }.take(15),
            recentRadios = recents.take(10).mapNotNull { recent ->
                allEntities.find { it.id == recent.radioId }?.toDomain()
            },
            regions = cachedRegions,
            genres = cachedGenres,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        viewModelScope.launch {
            repository.loadRadiosIfEmpty()
        }
    }
}
