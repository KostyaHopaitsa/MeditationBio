package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.BreathAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.ZSignalResult
import javax.inject.Inject

class CollectZValuesUseCase @Inject constructor(
    private val breathAnalyzerCore: BreathAnalyzerCore
) {
    operator fun invoke(z: Double): ZSignalResult {
        return breathAnalyzerCore.collectZValues(z)
    }
}