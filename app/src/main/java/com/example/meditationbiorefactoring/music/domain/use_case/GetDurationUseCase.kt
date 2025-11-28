package com.example.meditationbiorefactoring.music.domain.use_case

import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository

class GetDurationUseCase(
    private val repository: MusicPlayerRepository
) {
    operator fun invoke(): Long {
        return repository.getDuration()
    }
}