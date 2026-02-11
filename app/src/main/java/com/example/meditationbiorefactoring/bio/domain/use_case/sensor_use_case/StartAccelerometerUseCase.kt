package com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case

import com.example.meditationbiorefactoring.bio.domain.repository.AccelerometerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartAccelerometerUseCase @Inject constructor(
    private val repository: AccelerometerRepository
) {
    operator fun invoke(): Flow<FloatArray> {
        return repository.start()
    }
}