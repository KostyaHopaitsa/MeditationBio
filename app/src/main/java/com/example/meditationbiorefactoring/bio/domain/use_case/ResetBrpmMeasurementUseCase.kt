package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.BreathAnalyzerCore
import javax.inject.Inject

class ResetBrpmMeasurementUseCase @Inject constructor(
    private val breathAnalyzerCore: BreathAnalyzerCore
) {
    operator fun invoke() {
        breathAnalyzerCore.reset()
    }
}