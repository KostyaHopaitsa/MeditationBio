package com.example.meditationbiorefactoring.music.presentation

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import coil.request.CachePolicy
import com.example.meditationbiorefactoring.music.domain.model.Track
import com.example.meditationbiorefactoring.music.domain.use_case.GetTagByStressLevelUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.GetTracksByTagUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.PlayerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class MusicViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getTracksByTagUseCase: GetTracksByTagUseCase,
    private val playerUseCases: PlayerUseCases,
    private val getTagByStressLevelUseCase: GetTagByStressLevelUseCase
): ViewModel() {
    private val _state = mutableStateOf(MusicState())
    val state: State<MusicState> = _state

    private val _progress = mutableFloatStateOf(0f)
    val progress: State<Float> = _progress

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
                _state.value = _state.value.copy(duration = duration)
                _progress.floatValue = if (duration > 0) position / duration else 0f

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
            .onEach { tracks ->
                _state.value = _state.value.copy(
                    tracks = tracks,
                    isLoading = false,
                    error = if (tracks.isEmpty()) "No tracks found" else null
                )
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
            .launchIn(viewModelScope)
    }

    fun preloadImages(tracks: List<Track>) {
        val imageLoader = Coil.imageLoader(context)
        tracks.forEach { track ->
            track.imageUrl?.let { url ->
                imageLoader.enqueue(
                    ImageRequest.Builder(context)
                        .data(url)
                        .size(300)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .allowHardware(true)
                        .build()
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopObservingProgress()
        playerUseCases.stopUseCase()
        playerUseCases.releasePlayerUseCase()
    }
}