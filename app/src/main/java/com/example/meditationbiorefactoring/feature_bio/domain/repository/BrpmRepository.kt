package com.example.meditationbiorefactoring.feature_bio.domain.repository

import com.example.meditationbiorefactoring.feature_bio.domain.model.ZSignalResult

interface BrpmRepository {
    suspend fun collectZValues(z: Double): ZSignalResult
    suspend fun computeBrpm(values: List<Double>): Double
    suspend fun reset()
}