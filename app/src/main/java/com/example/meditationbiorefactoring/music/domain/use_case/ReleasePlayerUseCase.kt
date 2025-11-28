package com.example.meditationbiorefactoring.music.domain.use_case

import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository
import javax.inject.Inject

class ReleasePlayerUseCase @Inject constructor(
    private val repository: MusicPlayerRepository
) {
    operator fun invoke() {
        repository.release()
    }
}