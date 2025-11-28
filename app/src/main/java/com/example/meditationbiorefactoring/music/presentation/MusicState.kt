package com.example.meditationbiorefactoring.music.presentation

import com.example.meditationbiorefactoring.music.domain.model.Track

data class MusicState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = true,
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val isEnd: Boolean = false,
    val duration: Float = 0f,
    val error: String? = null
)
