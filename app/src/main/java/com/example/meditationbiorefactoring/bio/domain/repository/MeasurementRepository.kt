package com.example.meditationbiorefactoring.bio.domain.repository

import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.common.DataResult
import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {

    fun getMeasurements(): Flow<DataResult<List<Measurement>>>

    suspend fun getMeasurementById(id: Int): DataResult<Measurement?>

    suspend fun insertMeasurement(measurement: Measurement): DataResult<Unit>

    suspend fun deleteMeasurement(measurement: Measurement): DataResult<Unit>
}