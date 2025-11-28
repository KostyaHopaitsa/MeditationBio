package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.bio.domain.repository.BrpmRepository
import javax.inject.Inject

class ComputeBrpmUseCase @Inject constructor(
    private val repository: BrpmRepository
) {
    suspend operator fun invoke(
        zValues: List<Double>,
        progress: Float
    ): MeasurementAnalysis {
        return if (progress >= 1f) {
            val brpm = repository.computeBrpm(zValues)
            if (brpm in 5f..40f) {
                MeasurementAnalysis(MeasurementResult.Success(brpm), progress)
            } else {
                MeasurementAnalysis(MeasurementResult.Invalid(brpm), progress)
            }
        } else {
            MeasurementAnalysis(MeasurementResult.Error, progress)
        }
    }
}