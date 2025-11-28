package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.data.local.MeasurementDao
import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeasurementRepositoryImpl @Inject constructor(
    private val dao: MeasurementDao
) : MeasurementRepository {
    override fun getMeasurements(): Flow<List<Measurement>> {
        return dao.getMeasurements()
    }

    override suspend fun getMeasurementById(id: Int): Measurement? {
        return dao.getMeasurementById(id)
    }

    override suspend fun insertMeasurement(measurement: Measurement) {
        dao.insertMeasurement(measurement)
    }

//    override suspend fun deleteMeasurement(measurement: Measurement) {
//        dao.deleteMeasurement(measurement)
//    }

}