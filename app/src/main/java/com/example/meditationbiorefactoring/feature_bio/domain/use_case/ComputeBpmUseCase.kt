package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.feature_bio.domain.repository.BpmRepository
import javax.inject.Inject

class ComputeBpmUseCase @Inject constructor(
    private val repository: BpmRepository
) {
    suspend operator fun invoke(
        signal: List<Double>,
        timestamps: List<Long>,
        progress: Float
    ): MeasurementAnalysis {
        return if (progress >= 1f) {
            val bpm = repository.computeBpm(signal, timestamps)
            if (bpm in 40f..150f) {
                MeasurementAnalysis(MeasurementResult.Success(bpm), progress)
            } else {
                MeasurementAnalysis(MeasurementResult.Invalid(bpm), progress)
            }
        } else {
            MeasurementAnalysis(MeasurementResult.Error, progress)
        }
    }
}