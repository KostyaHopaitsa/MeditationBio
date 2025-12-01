package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.SivAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementResult
import javax.inject.Inject

class ComputeSivUseCase @Inject constructor(
    private val sivAnalyzerCore: SivAnalyzerCore
) {
    operator fun invoke(
        buffer: ShortArray,
        length: Int
    ): MeasurementAnalysis {
        return if (length > 0 || buffer.isNotEmpty()) {
            val siv = sivAnalyzerCore.computeSiv(buffer, length)
            if (siv in 0.01..0.15) {
                MeasurementAnalysis(MeasurementResult.Success(siv))
            } else {
                MeasurementAnalysis(MeasurementResult.Invalid(siv))
            }
        } else {
            MeasurementAnalysis(MeasurementResult.Error)
        }
    }
}