package com.example.meditationbiorefactoring.bio.domain.repository

import com.example.meditationbiorefactoring.common.DataResult
import kotlinx.coroutines.flow.Flow

interface AccelerometerRepository {
    fun start(): Flow<DataResult<FloatArray>>
    fun stop()
}