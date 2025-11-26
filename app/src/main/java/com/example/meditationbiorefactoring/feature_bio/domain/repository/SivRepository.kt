package com.example.meditationbiorefactoring.feature_bio.domain.repository

interface SivRepository {
    suspend fun startRecording()
    suspend fun stopRecording()
    suspend fun getRawData(): Pair<ShortArray, Int>
    suspend fun computeSiv(buffer: ShortArray, length: Int): Double
    suspend fun reset()
}