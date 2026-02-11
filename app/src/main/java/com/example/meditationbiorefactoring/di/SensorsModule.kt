package com.example.meditationbiorefactoring.di

import com.example.meditationbiorefactoring.bio.data.sensor.AudioSensor
import android.content.Context
import com.example.meditationbiorefactoring.bio.data.sensor.AccelerometerSensor
import com.example.meditationbiorefactoring.bio.data.repository.AccelerometerRepositoryImpl
import com.example.meditationbiorefactoring.bio.data.repository.AudioRepositoryImpl
import com.example.meditationbiorefactoring.bio.domain.repository.AccelerometerRepository
import com.example.meditationbiorefactoring.bio.domain.repository.AudioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorsModule {

    @Provides
    @Singleton
    fun provideAudioObserver(): AudioSensor = AudioSensor()

    @Provides
    @Singleton
    fun provideAudioRepository(audioObserver: AudioSensor): AudioRepository =
        AudioRepositoryImpl(audioObserver)

    @Provides
    @Singleton
    fun provideAccelerometerObserver(
        @ApplicationContext context: Context
    ): AccelerometerSensor = AccelerometerSensor(context)

    @Provides
    @Singleton
    fun provideAccelerometerRepository(
        accelerometerObserver: AccelerometerSensor
    ): AccelerometerRepository = AccelerometerRepositoryImpl(accelerometerObserver)
}