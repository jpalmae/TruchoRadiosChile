package cl.truchoradios.chile.presentation.screens.player

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.domain.model.Radio
import cl.truchoradios.chile.domain.model.StreamType
import cl.truchoradios.chile.player.RadioPlayerManager
import cl.truchoradios.chile.service.RadioPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val canSeekBack: Boolean = false,
    val rewindSeconds: Int = 0,
)

@HiltViewModel
class FullPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RadioRepositoryImpl,
    private val playerManager: RadioPlayerManager,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    private val radioId: String = savedStateHandle["radioId"] ?: ""

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState

    private var hasAutoPlayed = false

    init {
        viewModelScope.launch {
            val entity = repository.getRadioById(radioId)
            if (entity != null) {
                val radio = Radio(
                    id = entity.id, name = entity.name,
                    streamUrl = entity.streamUrl,
                    streamType = StreamType.valueOf(entity.streamType),
                    imageUrl = entity.imageUrl, frequency = entity.frequency,
                    city = entity.city, region = entity.region,
                    genres = entity.genres.split(",").filter { it.isNotBlank() },
                    website = entity.website ?: "", listeners = entity.listeners,
                )
                _uiState.value = _uiState.value.copy(radio = radio)

                // Auto-play on first load
                if (!hasAutoPlayed) {
                    hasAutoPlayed = true
                    startPlayback(radio)
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

        viewModelScope.launch {
            playerManager.canSeekBack.collect { can ->
                _uiState.value = _uiState.value.copy(canSeekBack = can)
            }
        }

        viewModelScope.launch {
            playerManager.rewindSeconds.collect { seconds ->
                _uiState.value = _uiState.value.copy(rewindSeconds = seconds)
            }
        }
    }

    private fun startPlayback(radio: Radio) {
        val sessionToken = SessionToken(
            appContext,
            ComponentName(appContext, RadioPlayerService::class.java)
        )
        val controllerFuture = MediaController.Builder(appContext, sessionToken).buildAsync()
        controllerFuture.addListener({
            try { controllerFuture.get() } catch (_: Exception) { }
        }, { it.run() })

        playerManager.play(radio)
    }

    fun play() {
        _uiState.value.radio?.let { startPlayback(it) }
    }

    fun stop() {
        playerManager.stop()
    }

    fun seekBack10() { playerManager.seekBackSeconds(10) }
    fun seekBack30() { playerManager.seekBackSeconds(30) }
    fun seekBack60() { playerManager.seekBackSeconds(60) }
    fun seekToLive() { playerManager.seekToLive() }

    fun toggleFavorite() {
        val current = _uiState.value.isFavorite
        _uiState.value = _uiState.value.copy(isFavorite = !current)
        viewModelScope.launch {
            if (!current) repository.addFavorite(radioId) else repository.removeFavorite(radioId)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
