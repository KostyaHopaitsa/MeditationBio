package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

import com.example.meditationbiorefactoring.bio.domain.core.BreathAnalyzerCore
import com.example.meditationbiorefactoring.common.DomainError
import com.example.meditationbiorefactoring.common.DomainResult
import javax.inject.Inject

class ComputeBrpmUseCase @Inject constructor(
    private val breathAnalyzerCore: BreathAnalyzerCore
) {
    operator fun invoke(
        zValues: List<Double>,
    ): DomainResult<Double> {
        val brpm = breathAnalyzerCore.computeBrpm(zValues)
        return if (brpm in 5f..40f) {
            DomainResult.Success(brpm)
        } else {
            DomainResult.Error(DomainError.MeasureFailed)
        }
    }
}