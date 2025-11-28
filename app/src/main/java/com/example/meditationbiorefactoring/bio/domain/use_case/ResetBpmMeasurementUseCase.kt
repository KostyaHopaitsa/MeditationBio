package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.repository.BpmRepository
import javax.inject.Inject

class ResetBpmMeasurementUseCase @Inject constructor(
    private val repository: BpmRepository
) {
    suspend operator fun invoke() {
        repository.reset()
    }
}