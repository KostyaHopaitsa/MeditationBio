package com.example.meditationbiorefactoring.bio.data.repository

import com.example.meditationbiorefactoring.bio.data.local.MeasurementDao
import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import com.example.meditationbiorefactoring.common.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MeasurementRepositoryImpl @Inject constructor(
    private val dao: MeasurementDao
) : MeasurementRepository {
    override fun getMeasurements(): Flow<DataResult<List<Measurement>>> {
        return dao.getMeasurements()
            .map<List<Measurement>, DataResult<List<Measurement>>> { list ->
                DataResult.Success(list)
            }
            .catch { throwable ->
                emit(DataResult.Error(throwable))
            }
    }

    override suspend fun getMeasurementById(id: Int): DataResult<Measurement?> {
        return try {
            val result = dao.getMeasurementById(id)
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun insertMeasurement(measurement: Measurement): DataResult<Unit> {
        return try {
            dao.insertMeasurement(measurement)
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun deleteMeasurement(measurement: Measurement): DataResult<Unit> {
        return try {
            dao.deleteMeasurement(measurement)
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}