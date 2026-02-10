package com.example.meditationbiorefactoring.di

import AudioObserverImpl
import android.content.Context
import com.example.meditationbiorefactoring.bio.data.observer.AccelerometerObserverImpl
import com.example.meditationbiorefactoring.bio.domain.observer.AccelerometerObserver
import com.example.meditationbiorefactoring.bio.domain.observer.AudioObserver
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
    fun provideAudioObserver(): AudioObserver = AudioObserverImpl()

    @Provides
    @Singleton
    fun provideAccelerometerObserver(
        @ApplicationContext context: Context
    ): AccelerometerObserver = AccelerometerObserverImpl(context)

}