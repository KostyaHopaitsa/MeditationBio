package com.example.meditationbiorefactoring.music.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toUiError
import com.example.meditationbiorefactoring.music.domain.use_case.GetTagByStressLevelUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.track_use_case.GetTracksByTagUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.PlayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val getTracksByTagUseCase: GetTracksByTagUseCase,
    private val playerUseCases: PlayerUseCases,
    private val getTagByStressLevelUseCase: GetTagByStressLevelUseCase
): ViewModel() {
    private val _state = MutableStateFlow(MusicState())
    val state: StateFlow<MusicState> = _state

    private var progressJob: Job? = null
    private var loadTracksJob: Job? = null
    private var lastTag: String? = null

    fun onEvent(event: MusicEvent) {
        when(event) {
            is MusicEvent.TrackClick -> {
                playerUseCases.playUseCase(event.track.audioUrl)
                startObservingProgress()
                _state.value = _state.value.copy(
                    currentTrack = event.track,
                    isPlaying = true,
                    isEnd = false
                )
            }
            is MusicEvent.Pause -> {
                playerUseCases.pauseUseCase()
                _state.value = _state.value.copy(isPlaying = playerUseCases.isPlayingUseCase())
            }
            is MusicEvent.Resume -> {
                playerUseCases.resumeUseCase()
                _state.value = _state.value.copy(
                    isPlaying = playerUseCases.isPlayingUseCase(),
                    isEnd = false
                )
                startObservingProgress()
            }
            is MusicEvent.SeekTo -> {
                playerUseCases. seekToUseCase(event.positionMs)
            }
            is MusicEvent.TrackEnd -> {
                playerUseCases.pauseUseCase()
                _state.value = _state.value.copy(isPlaying = playerUseCases.isPlayingUseCase())
                playerUseCases.seekToUseCase(0L)
                _state.value = _state.value.copy(
                    isEnd = true
                )
                stopObservingProgress()
            }
            MusicEvent.Retry -> {
                lastTag?.let { tag ->
                    loadTracks(tag)
                }
            }
        }
    }

    private fun startObservingProgress() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                val position = playerUseCases.getCurrentPositionUseCase().toFloat()
                val duration = playerUseCases.getDurationUseCase().toFloat()
                _state.value = _state.value.copy(
                    duration = duration,
                    progress = if (duration > 0) position / duration else 0f
                )

                if (position >= duration && duration > 0 && !_state.value.isEnd) {
                    onEvent(MusicEvent.TrackEnd)
                }
                delay(100L)
            }
        }
    }

    private fun stopObservingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    fun loadMusic(stressLevel: String?) {
        viewModelScope.launch {
            val tag =
                if (stressLevel != null) getTagByStressLevelUseCase(stressLevel)
                else "ambient+downtempo+calm"
            loadTracks(tag)
        }
    }

    private fun loadTracks(tag: String) {
        lastTag = tag
        _state.value = _state.value.copy(isLoading = true, error = null)

        loadTracksJob?.cancel()
        loadTracksJob = getTracksByTagUseCase(tag)
            .onEach { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _state.value = _state.value.copy(
                            tracks = result.data,
                            isLoading = false,
                            error = if (result.data.isEmpty()) "No tracks found" else null
                        )
                    }
                    is DomainResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.error.toUiError().message
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        stopObservingProgress()
        playerUseCases.stopUseCase()
        playerUseCases.releasePlayerUseCase()
    }
}