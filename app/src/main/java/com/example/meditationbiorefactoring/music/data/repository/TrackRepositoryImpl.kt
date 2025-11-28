package com.example.meditationbiorefactoring.music.data.repository

import android.util.Log
import com.example.meditationbiorefactoring.music.data.remote.JamendoApi
import com.example.meditationbiorefactoring.music.data.remote.dto.toTrack
import com.example.meditationbiorefactoring.music.domain.model.Track
import com.example.meditationbiorefactoring.music.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val api: JamendoApi
) : TrackRepository {
    override fun getTracksByTag(tag: String): Flow<List<Track>> = flow {
        val response = api.getTracksByTag(tag = tag)
        emit(response.results.map { it.toTrack() })
    }.catch { e ->
        Log.e("MusicDebug", "Repository error", e)
        emit(emptyList())
    }
}
