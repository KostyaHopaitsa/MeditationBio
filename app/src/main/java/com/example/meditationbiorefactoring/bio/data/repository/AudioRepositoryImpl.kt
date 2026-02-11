package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.data.sensor.AudioSensor
import com.example.meditationbiorefactoring.bio.domain.repository.AudioRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AudioRepositoryImpl(
    private val audioObserver: AudioSensor
): AudioRepository {
    override fun start(): Flow<ShortArray> = callbackFlow {
        audioObserver.start { trySend(it).isSuccess }
        awaitClose { audioObserver.stop() }
    }
    override fun stop() {
        audioObserver.stop()
    }
}