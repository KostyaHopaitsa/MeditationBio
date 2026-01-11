package com.example.meditationbiorefactoring.music.presentation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meditationbiorefactoring.common.presentation.components.Error
import com.example.meditationbiorefactoring.music.presentation.components.MusicItem
import com.example.meditationbiorefactoring.music.presentation.components.PlayerBar

@Composable
fun MusicScreen(
    viewModel: MusicViewModel = hiltViewModel(),
    stressLevel: String? = null,
) {
    val state = viewModel.state.value

    LaunchedEffect(stressLevel) {
        viewModel.loadMusic(stressLevel)
    }

    LaunchedEffect(state.tracks) {
        if (state.tracks.isNotEmpty()) {
            viewModel.preloadImages(state.tracks)
        }
    }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Error(
                message = state.error,
                onRetry = { viewModel.onEvent(MusicEvent.Retry) }
            )
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.tracks) { track ->
                    MusicItem(
                        track = track,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .size(120.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current,
                            ) {
                                viewModel.onEvent(MusicEvent.TrackClick(track))
                            }
                    )
                }
            }
            state.currentTrack?.let {
                PlayerBar(
                    track = it,
                    progress = viewModel.progress.value,
                    buttonIcon = if (state.isPlaying) Icons.Default.Pause
                    else if (state.isEnd) Icons.Default.Replay
                    else Icons.Default.PlayArrow,
                    onPlayControl = {
                        if (state.isPlaying) {
                            viewModel.onEvent(MusicEvent.Pause)
                        } else {
                            viewModel.onEvent(MusicEvent.Resume)
                        }
                    },
                    onSeek = { newProgress ->
                        val posMs = (newProgress * state.duration).toLong()
                        viewModel.onEvent(MusicEvent.SeekTo(positionMs = posMs))
                    }
                )
            }
        }
    }
}