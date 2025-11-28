package com.example.meditationbiorefactoring.music.domain.use_case

import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository

class GetCurrentPositionUseCase(
    private val repository: MusicPlayerRepository
) {
    operator fun invoke(): Long {
        return repository.getCurrentPosition()
    }
}