package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

import com.example.meditationbiorefactoring.bio.domain.core.SivAnalyzerCore
import javax.inject.Inject

class ResetSivMeasurementUseCase @Inject constructor(
    private val sivAnalyzerCore: SivAnalyzerCore
) {
    operator fun invoke() {
        sivAnalyzerCore.reset()
    }
}