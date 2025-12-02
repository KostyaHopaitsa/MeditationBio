package com.example.meditationbiorefactoring.di

import AudioRecorderController
import android.content.Context
import com.example.meditationbiorefactoring.bio.data.controller.AccelerometerController
import com.example.meditationbiorefactoring.bio.domain.sensors.Accelerometer
import com.example.meditationbiorefactoring.bio.domain.sensors.AudioRecorder
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
    fun provideAudioRecorder(): AudioRecorder = AudioRecorderController()

    @Provides
    @Singleton
    fun provideAccelerometer(
        @ApplicationContext context: Context
    ): Accelerometer = AccelerometerController(context)
}