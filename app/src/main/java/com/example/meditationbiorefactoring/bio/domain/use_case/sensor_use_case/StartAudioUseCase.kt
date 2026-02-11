package com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case

import com.example.meditationbiorefactoring.bio.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartAudioUseCase @Inject constructor(
    private val repository: AudioRepository
) {
    operator fun invoke(): Flow<ShortArray> {
        return repository.start()
    }
}