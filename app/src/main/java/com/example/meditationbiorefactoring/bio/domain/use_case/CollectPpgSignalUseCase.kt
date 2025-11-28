package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.model.PpgSignalResult
import com.example.meditationbiorefactoring.bio.domain.repository.BpmRepository
import javax.inject.Inject

class CollectPpgSignalUseCase @Inject constructor(
    private val repository: BpmRepository
) {
    suspend operator fun invoke(buffer: ByteArray): PpgSignalResult {
        return repository.collectPpgSignal(buffer)
    }
}