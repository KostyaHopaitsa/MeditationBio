package com.example.meditationbiorefactoring.bio.domain.repository

import kotlinx.coroutines.flow.Flow

interface AccelerometerRepository {
    fun start(): Flow<FloatArray>
    fun stop()
}