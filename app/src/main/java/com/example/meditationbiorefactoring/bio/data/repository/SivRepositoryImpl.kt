package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.domain.repository.SivRepository
import com.example.meditationbiorefactoring.bio.data.analyzer.SivAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.SivRawData
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

    override suspend fun getRawData(): SivRawData {
        return analyzer.getRawData()
    }

    override suspend fun computeSiv(buffer: ShortArray, length: Int): Double {
        return analyzer.computeSiv(buffer, length)
    }

    override suspend fun reset() {
        analyzer.reset()
    }
}