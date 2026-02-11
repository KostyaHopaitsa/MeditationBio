package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.data.sensor.AccelerometerSensor
import com.example.meditationbiorefactoring.bio.domain.repository.AccelerometerRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AccelerometerRepositoryImpl(
    private val accelerometerObserver: AccelerometerSensor
): AccelerometerRepository{
    override fun start(): Flow<FloatArray> = callbackFlow {
        accelerometerObserver.start { trySend(it).isSuccess }
        awaitClose { accelerometerObserver.stop() }
    }
    override fun stop() {
        accelerometerObserver.stop()
    }
}