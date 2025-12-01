package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.PpgAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementResult
import javax.inject.Inject

class ComputeBpmUseCase @Inject constructor(
    private val ppgAnalyzerCore: PpgAnalyzerCore
) {
    operator fun invoke(
        signal: List<Double>,
        timestamps: List<Long>,
        progress: Float
    ): MeasurementAnalysis {
        return if (progress >= 1f) {
            val bpm = ppgAnalyzerCore.computeBpm(signal, timestamps)
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