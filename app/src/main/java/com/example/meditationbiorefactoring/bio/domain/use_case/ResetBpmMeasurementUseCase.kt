package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.PpgAnalyzerCore
import javax.inject.Inject

class ResetBpmMeasurementUseCase @Inject constructor(
    private val ppgAnalyzerCore: PpgAnalyzerCore
) {
    operator fun invoke() {
        ppgAnalyzerCore.reset()
    }
}