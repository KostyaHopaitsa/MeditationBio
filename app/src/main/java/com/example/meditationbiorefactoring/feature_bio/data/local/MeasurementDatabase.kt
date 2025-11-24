package com.example.meditationbiorefactoring.feature_bio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.meditationbiorefactoring.feature_bio.domain.model.Measurement

@Database(
    entities = [Measurement::class],
    version = 1
)
abstract class MeasurementDatabase : RoomDatabase() {

    abstract fun measurementDao(): MeasurementDao

    companion object {
        const val DATABASE_NAME = "measurement_db"
    }
}