package com.example.meditationbiorefactoring.music.data.repository

import com.example.meditationbiorefactoring.common.DataResult
import com.example.meditationbiorefactoring.music.data.remote.JamendoApi
import com.example.meditationbiorefactoring.music.data.remote.dto.toTrack
import com.example.meditationbiorefactoring.music.domain.model.Track
import com.example.meditationbiorefactoring.music.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TrackRepositoryImpl(
    private val api: JamendoApi
) : TrackRepository {
    override fun getTracksByTag(tag: String): Flow<DataResult<List<Track>>> {
        return flow {
            val response = api.getTracksByTag(tag = tag)
            emit(response.results.map { it.toTrack() })
        }
            .map<List<Track>, DataResult<List<Track>>> { list ->
                DataResult.Success(list)
            }
            .catch { throwable ->
                emit(DataResult.Error(throwable))
            }
    }
}
