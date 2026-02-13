package com.example.meditationbiorefactoring.bio.domain.use_case.measurement_use_case

import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import com.example.meditationbiorefactoring.common.DataResult
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toDomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMeasurementsUseCase @Inject constructor(
    private val repository: MeasurementRepository
) {
    operator fun invoke(): Flow<DomainResult<List<Measurement>>> {
        return repository.getMeasurements().map { result ->
            when (result) {
                is DataResult.Success -> DomainResult.Success(result.data)
                is DataResult.Error -> DomainResult.Error(result.throwable.toDomainError())
            }
        }
    }
}