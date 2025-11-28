package com.example.meditationbiorefactoring.music.domain.repository

import com.example.meditationbiorefactoring.music.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTracksByTag(tag: String): Flow<List<Track>>
}