package com.example.meditationbiorefactoring.feature_bio.data.repository

import com.example.meditationbiorefactoring.feature_bio.domain.repository.BpmRepository
import com.example.meditationbiorefactoring.feature_bio.data.analyzer.PpgAnalyzerCore
import com.example.meditationbiorefactoring.feature_bio.domain.model.PpgSignalResult
import javax.inject.Inject

class BpmRepositoryImpl @Inject constructor(
    private val analyzer: PpgAnalyzerCore
) : BpmRepository {

    override suspend fun computeBpm(signal: List<Double>, timestamps: List<Long>): Double {
        return analyzer.computeBpm(signal, timestamps)
    }

    override suspend fun collectPpgSignal(buffer: ByteArray): PpgSignalResult {
        return analyzer.collectPpgSignal(buffer)
    }

    override suspend fun reset() {
        analyzer.reset()
    }
}