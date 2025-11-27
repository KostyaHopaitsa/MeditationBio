package com.example.meditationbiorefactoring.feature_bio.domain.repository

import com.example.meditationbiorefactoring.feature_bio.domain.model.PpgSignalResult

interface BpmRepository {
    suspend fun computeBpm(signal: List<Double>, times: List<Long>): Double
    suspend fun collectPpgSignal(buffer: ByteArray): PpgSignalResult
    suspend fun reset()
}