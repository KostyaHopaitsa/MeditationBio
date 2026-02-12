package com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case

import com.example.meditationbiorefactoring.bio.domain.repository.AudioRepository
import com.example.meditationbiorefactoring.common.DataResult
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toDomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StartAudioUseCase @Inject constructor(
    private val repository: AudioRepository
) {
    operator fun invoke(): Flow<DomainResult<ShortArray>> {
        return repository.start().map { result ->
            when(result) {
                is DataResult.Success -> DomainResult.Success(result.data)
                is DataResult.Error -> DomainResult.Error(result.throwable.toDomainError())
            }
        }
    }
}