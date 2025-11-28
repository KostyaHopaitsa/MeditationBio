package com.example.meditationbiorefactoring.music.domain.use_case

import android.util.Log
import com.example.meditationbiorefactoring.music.domain.model.Track
import com.example.meditationbiorefactoring.music.domain.repository.TrackRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTracksByTagUseCase @Inject constructor(
    private val repository: TrackRepository
) {
    operator fun invoke(
        tag: String,
        maxRetries: Int = 5,
        retryDelay: Long = 500L
    ): Flow<List<Track>> = flow {
        var attempt = 0
        var result: List<Track>

        do {
            result = try {
                repository.getTracksByTag(tag).first()
            } catch (e: Exception) {
                Log.e("GetTracksByTagUseCase", "Error on attempt $attempt: ${e.message}")
                emptyList()
            }

            if (result.isEmpty() && attempt < maxRetries) {
                Log.w("GetTracksByTagUseCase", "Empty result, retrying (${attempt + 1}/$maxRetries)...")
                delay(retryDelay)
            }

            attempt++
        } while (result.isEmpty() && attempt <= maxRetries)

        emit(result)
    }
}