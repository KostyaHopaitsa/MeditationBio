package com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case

import com.example.meditationbiorefactoring.bio.domain.repository.AccelerometerRepository
import javax.inject.Inject

class StopAccelerometerUseCase @Inject constructor(
    private val repository: AccelerometerRepository
) {
    operator fun invoke() {
        return repository.stop()
    }
}