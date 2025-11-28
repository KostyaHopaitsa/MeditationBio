package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.repository.SivRepository
import javax.inject.Inject

class StartSivRecordingUseCase @Inject constructor(
    private val repository: SivRepository
) {
    suspend operator fun invoke() {
        repository.startRecording()
    }
}