package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.SivRawData
import com.example.meditationbiorefactoring.feature_bio.domain.repository.SivRepository
import javax.inject.Inject

class GetRawSivDataUseCase @Inject constructor(
    private val repository: SivRepository
) {
    suspend operator fun invoke(): SivRawData {
        return repository.getRawData()
    }
}