package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.data.sensor.AccelerometerSensor
import com.example.meditationbiorefactoring.bio.domain.repository.AccelerometerRepository
import com.example.meditationbiorefactoring.common.DataResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AccelerometerRepositoryImpl(
    private val accelerometerSensor: AccelerometerSensor
): AccelerometerRepository{
    override fun start(): Flow<DataResult<FloatArray>> = callbackFlow {
        try {
            accelerometerSensor.start { data ->
                trySend(DataResult.Success(data))
            }
        } catch (e: Exception) {
            trySend(DataResult.Error(e))
        }

        awaitClose { accelerometerSensor.stop() }
    }
    override fun stop() {
        accelerometerSensor.stop()
    }
}