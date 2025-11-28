package com.example.meditationbiorefactoring.bio.domain.repository

import com.example.meditationbiorefactoring.bio.domain.model.SivRawData

interface SivRepository {
    suspend fun startRecording()
    suspend fun stopRecording()
    suspend fun getRawData(): SivRawData
    suspend fun computeSiv(buffer: ShortArray, length: Int): Double
    suspend fun reset()
}