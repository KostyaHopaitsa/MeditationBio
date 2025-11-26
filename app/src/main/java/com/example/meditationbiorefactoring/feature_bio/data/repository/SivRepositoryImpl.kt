package com.example.meditationbiorefactoring.feature_bio.data.repository

import com.example.meditationbiorefactoring.feature_bio.domain.repository.SivRepository
import com.example.meditationbiorefactoring.feature_bio.data.analyzer.SivAnalyzerCore
import javax.inject.Inject

class SivRepositoryImpl @Inject constructor(
    private val analyzer: SivAnalyzerCore
) : SivRepository {

    override suspend fun startRecording() {
        analyzer.startRecording()
    }

    override suspend fun stopRecording() {
        analyzer.stopRecording()
    }

    override suspend fun getRawData(): Pair<ShortArray, Int> {
        return analyzer.getRawData()
    }

    override suspend fun computeSiv(buffer: ShortArray, length: Int): Double {
        return analyzer.computeSiv(buffer, length)
    }

    override suspend fun reset() {
        analyzer.reset()
    }
}