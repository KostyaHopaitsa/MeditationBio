package com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case

import com.example.meditationbiorefactoring.bio.domain.repository.AudioRepository
import javax.inject.Inject

class StopAudioUseCase @Inject constructor(
    private val repository: AudioRepository
) {
    operator fun invoke() {
        return repository.stop()
    }
}