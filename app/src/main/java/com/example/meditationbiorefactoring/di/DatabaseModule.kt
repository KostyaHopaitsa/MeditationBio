package com.example.meditationbiorefactoring.di

import android.app.Application
import androidx.room.Room
import com.example.meditationbiorefactoring.bio.data.local.MeasurementDatabase
import com.example.meditationbiorefactoring.bio.data.repository.MeasurementRepositoryImpl
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMeasurementDatabase(app: Application): MeasurementDatabase {
        return Room.databaseBuilder(
            app,
            MeasurementDatabase::class.java,
            MeasurementDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideMeasurementRepository(db: MeasurementDatabase): MeasurementRepository {
        return MeasurementRepositoryImpl(db.measurementDao())
    }
}