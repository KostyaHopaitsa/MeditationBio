package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.domain.repository.BrpmRepository
import com.example.meditationbiorefactoring.bio.data.analyzer.BreathAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.ZSignalResult
import javax.inject.Inject

class BrpmRepositoryImpl @Inject constructor(
    private val analyzer: BreathAnalyzerCore
) : BrpmRepository {

    override suspend fun collectZValues(z: Double): ZSignalResult {
        return analyzer.collectZValues(z)
    }

    override suspend fun computeBrpm(values: List<Double>): Double {
        return analyzer.computeBrpm(values)
    }

    override suspend fun reset() {
        analyzer.reset()
    }
}
