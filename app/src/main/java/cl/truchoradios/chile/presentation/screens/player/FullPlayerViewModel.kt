package cl.truchoradios.chile.presentation.screens.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.truchoradios.chile.data.local.entity.toDomain
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.player.RadioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val radio: Radio? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
)

@HiltViewModel
class FullPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RadioRepositoryImpl,
    private val playerManager: RadioPlayerManager,
) : ViewModel() {

    private val radioId: String = savedStateHandle["radioId"] ?: ""

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState

    private var hasAutoPlayed = false

    init {
        viewModelScope.launch {
            val entity = repository.getRadioById(radioId)
            if (entity != null) {
                val radio = entity.toDomain()
                _uiState.value = _uiState.value.copy(radio = radio)

                if (!hasAutoPlayed) {
                    hasAutoPlayed = true
                    playerManager.play(radio)
                    repository.addRecent(radioId)
                }
            }
        }

        viewModelScope.launch {
            playerManager.isPlaying.collect { playing ->
                _uiState.value = _uiState.value.copy(isPlaying = playing)
            }
        }

        viewModelScope.launch {
            playerManager.isBuffering.collect { buffering ->
                _uiState.value = _uiState.value.copy(isBuffering = buffering)
            }
        }

        viewModelScope.launch {
            playerManager.error.collect { err ->
                _uiState.value = _uiState.value.copy(error = err)
            }
        }
    }

    fun play() {
        _uiState.value.radio?.let { radio ->
            val current = playerManager.getCurrentRadio()
            if (current?.id == radio.id && playerManager.playbackState.value.name == "PAUSED") {
                playerManager.resume()
            } else {
                playerManager.play(radio)
            }
        }
    }

    fun stop() {
        playerManager.pause()
    }

    fun toggleFavorite() {
        val current = _uiState.value.isFavorite
        _uiState.value = _uiState.value.copy(isFavorite = !current)
        viewModelScope.launch {
            if (!current) repository.addFavorite(radioId) else repository.removeFavorite(radioId)
        }
    }
}
