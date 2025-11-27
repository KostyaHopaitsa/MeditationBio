package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.PpgSignalResult
import com.example.meditationbiorefactoring.feature_bio.domain.repository.BpmRepository
import javax.inject.Inject

class CollectPpgSignalUseCase @Inject constructor(
    private val repository: BpmRepository
) {
    suspend operator fun invoke(buffer: ByteArray): PpgSignalResult {
        return repository.collectPpgSignal(buffer)
    }
}