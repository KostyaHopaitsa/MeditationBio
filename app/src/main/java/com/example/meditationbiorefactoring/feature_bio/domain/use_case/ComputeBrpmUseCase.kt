package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.feature_bio.domain.repository.BrpmRepository
import javax.inject.Inject

class ComputeBrpmUseCase @Inject constructor(
    private val repository: BrpmRepository
) {
    suspend operator fun invoke(z: Float): MeasurementAnalysis {
        return repository.processFrame(z)
    }
}