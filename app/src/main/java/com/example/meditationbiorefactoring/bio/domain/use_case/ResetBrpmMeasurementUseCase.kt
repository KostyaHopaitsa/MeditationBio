package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.repository.BrpmRepository
import javax.inject.Inject

class ResetBrpmMeasurementUseCase @Inject constructor(
    private val repository: BrpmRepository
) {
    suspend operator fun invoke() {
        repository.reset()
    }
}