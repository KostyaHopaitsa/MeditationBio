package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.feature_bio.domain.repository.BpmRepository
import javax.inject.Inject

class BpmMeasurementUseCase @Inject constructor(
    private val repository: BpmRepository
) {
    suspend operator fun invoke(buffer: ByteArray): MeasurementAnalysis {
        return repository.processFrame(buffer)
    }
}