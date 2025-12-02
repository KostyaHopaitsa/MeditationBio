package com.example.meditationbiorefactoring.di

import com.example.meditationbiorefactoring.bio.domain.core.*
import com.example.meditationbiorefactoring.bio.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BioModule {

    @Provides
    @Singleton
    fun providePpgAnalyzer(): PpgAnalyzerCore = PpgAnalyzerCore()

    @Provides
    @Singleton
    fun provideBreathAnalyzer(): BreathAnalyzerCore = BreathAnalyzerCore()

    @Provides
    @Singleton
    fun provideSivAnalyzer(): SivAnalyzerCore = SivAnalyzerCore()

    @Provides
    @Singleton
    fun provideAudioCoreUseCase(core: SivAnalyzerCore): AudioCoreUseCases {
        return AudioCoreUseCases(
            addChunkUseCase = AddChunkUseCase(core),
            buildAudioBufferUseCase = BuildAudioBufferUseCase(core),
            computeSivUseCase = ComputeSivUseCase(core),
            resetSivMeasurementUseCase = ResetSivMeasurementUseCase(core)
        )
    }
}