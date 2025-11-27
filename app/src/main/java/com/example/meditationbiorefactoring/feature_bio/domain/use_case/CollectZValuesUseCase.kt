package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.ZSignalResult
import com.example.meditationbiorefactoring.feature_bio.domain.repository.BrpmRepository
import javax.inject.Inject

class CollectZValuesUseCase @Inject constructor(
    private val repository: BrpmRepository
) {
    suspend operator fun invoke(z: Double): ZSignalResult {
        return repository.collectZValues(z)
    }
}