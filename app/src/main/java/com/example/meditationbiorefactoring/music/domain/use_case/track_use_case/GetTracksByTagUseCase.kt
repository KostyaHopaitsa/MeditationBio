package com.example.meditationbiorefactoring.music.domain.use_case.track_use_case

import com.example.meditationbiorefactoring.common.DataResult
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toDomainError
import com.example.meditationbiorefactoring.music.domain.model.Track
import com.example.meditationbiorefactoring.music.domain.repository.TrackRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject

class GetTracksByTagUseCase @Inject constructor(
    private val repository: TrackRepository
) {
    operator fun invoke(
        tag: String,
        maxRetries: Int = 5,
        retryDelay: Long = 500L
    ): Flow<DomainResult<List<Track>>> {
        return repository.getTracksByTag(tag)
            .map<DataResult<List<Track>>, DomainResult<List<Track>>> { dataResult ->
                when (dataResult) {
                    is DataResult.Success -> DomainResult.Success(dataResult.data)
                    is DataResult.Error -> throw dataResult.throwable
                }
            }
            .retryWhen { cause, attempt ->
                val shouldRetry = (cause is IOException) && attempt < maxRetries
                if (shouldRetry) delay(retryDelay)
                shouldRetry
            }
            .catch { throwable ->
                emit(DomainResult.Error(throwable.toDomainError()))
            }
    }
}