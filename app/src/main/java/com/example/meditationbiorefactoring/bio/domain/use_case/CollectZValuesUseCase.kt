package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.model.ZSignalResult
import com.example.meditationbiorefactoring.bio.domain.repository.BrpmRepository
import javax.inject.Inject

class CollectZValuesUseCase @Inject constructor(
    private val repository: BrpmRepository
) {
    suspend operator fun invoke(z: Double): ZSignalResult {
        return repository.collectZValues(z)
    }
}