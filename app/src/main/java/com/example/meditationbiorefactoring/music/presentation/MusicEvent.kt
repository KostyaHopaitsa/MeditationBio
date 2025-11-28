package com.example.meditationbiorefactoring.music.presentation

import com.example.meditationbiorefactoring.music.domain.model.Track

sealed class MusicEvent {
    data object Pause : MusicEvent()
    data class SeekTo(val positionMs: Long) : MusicEvent()
    data object Resume : MusicEvent()
    data class TrackClick(val track: Track): MusicEvent()
    data object TrackEnd : MusicEvent()
    data object Retry : MusicEvent()
}