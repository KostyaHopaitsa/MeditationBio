package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.data.sensor.AudioSensor
import com.example.meditationbiorefactoring.bio.domain.repository.AudioRepository
import com.example.meditationbiorefactoring.common.DataResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AudioRepositoryImpl(
    private val audioSensor: AudioSensor
): AudioRepository {
    override fun start(): Flow<DataResult<ShortArray>> = callbackFlow {
        try {
            audioSensor.start { chunk ->
                trySend(DataResult.Success(chunk))
            }
        } catch (e: Throwable) {
            trySend(DataResult.Error(e))
        }

        awaitClose {
            audioSensor.stop()
        }
    }
    override fun stop() {
        audioSensor.stop()
    }
}