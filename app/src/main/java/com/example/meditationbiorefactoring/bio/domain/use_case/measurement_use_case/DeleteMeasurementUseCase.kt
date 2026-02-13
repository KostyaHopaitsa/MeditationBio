package com.example.meditationbiorefactoring.bio.domain.use_case.measurement_use_case

import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import com.example.meditationbiorefactoring.common.DataResult
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toDomainError
import javax.inject.Inject

class DeleteMeasurementUseCase @Inject constructor(
    private val repository: MeasurementRepository
) {
    suspend operator fun invoke(measurement: Measurement): DomainResult<Unit> {
        return when (val result =  repository.deleteMeasurement(measurement)) {
            is DataResult.Success -> DomainResult.Success(Unit)
            is DataResult.Error ->  DomainResult.Error(result.throwable.toDomainError())
        }
    }
}