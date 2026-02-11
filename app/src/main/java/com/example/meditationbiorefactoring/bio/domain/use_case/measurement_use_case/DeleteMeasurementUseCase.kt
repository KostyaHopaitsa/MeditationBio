package com.example.meditationbiorefactoring.bio.domain.use_case.measurement_use_case

import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import javax.inject.Inject

class DeleteMeasurementUseCase @Inject constructor(
    private val repository: MeasurementRepository
) {
    suspend operator fun invoke(measurement: Measurement) {
        repository.deleteMeasurement(measurement)
    }
}