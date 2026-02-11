package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

import com.example.meditationbiorefactoring.bio.domain.core.PpgAnalyzerCore
import com.example.meditationbiorefactoring.common.DomainError
import com.example.meditationbiorefactoring.common.DomainResult
import javax.inject.Inject

class ComputeBpmUseCase @Inject constructor(
    private val ppgAnalyzerCore: PpgAnalyzerCore
) {
    operator fun invoke(
        signal: List<Double>,
        timestamps: List<Long>,
        progress: Float
    ): DomainResult<Double> {
        return if (progress >= 1f) {
            val bpm = ppgAnalyzerCore.computeBpm(signal, timestamps)
            if (bpm in 40f..150f) {
                DomainResult.Success(bpm)
            } else {
                DomainResult.Error(DomainError.MeasureFailed)
            }
        } else {
            DomainResult.Error(DomainError.MeasureFailed)
        }
    }
}