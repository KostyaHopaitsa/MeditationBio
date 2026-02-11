package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

import com.example.meditationbiorefactoring.bio.domain.core.BreathAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.ZData
import javax.inject.Inject

class CollectZValuesUseCase @Inject constructor(
    private val breathAnalyzerCore: BreathAnalyzerCore
) {
    operator fun invoke(z: Double): ZData {
        return breathAnalyzerCore.collectZValues(z)
    }
}