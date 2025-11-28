package com.example.meditationbiorefactoring.bio.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {

    @Query("SELECT * FROM measurement")
    fun getMeasurements(): Flow<List<Measurement>>

    @Query("SELECT * FROM measurement WHERE id == :id")
    suspend fun getMeasurementById(id: Int): Measurement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement)

//    @Delete
//    suspend fun deleteMeasurement(measurement: Measurement)
}