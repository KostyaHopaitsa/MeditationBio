package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

import com.example.meditationbiorefactoring.bio.domain.core.SivAnalyzerCore
import com.example.meditationbiorefactoring.common.DomainError
import com.example.meditationbiorefactoring.common.DomainResult
import javax.inject.Inject

class ComputeSivUseCase @Inject constructor(
    private val sivAnalyzerCore: SivAnalyzerCore
) {
    operator fun invoke(
        buffer: ShortArray
    ): DomainResult<Double> {
        return if (buffer.isNotEmpty()) {
            val siv = sivAnalyzerCore.computeSiv(buffer)
            if (siv in 0.01..0.15) {
                DomainResult.Success(siv)
            } else {
                DomainResult.Error(DomainError.MeasureFailed)
            }
        } else {
            DomainResult.Error(DomainError.MeasureFailed)
        }
    }
}