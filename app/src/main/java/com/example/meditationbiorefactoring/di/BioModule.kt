package com.example.meditationbiorefactoring.di

import com.example.meditationbiorefactoring.bio.domain.core.*
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.AddChunkUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.AudioCoreUseCases
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.BreathCoreUseCases
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.BuildAudioBufferUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.CollectPpgSignalUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.CollectZValuesUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.ComputeBpmUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.ComputeBrpmUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.ComputeSivUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.PpgCoreUseCases
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.ResetBpmMeasurementUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.ResetBrpmMeasurementUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.ResetSivMeasurementUseCase
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
    fun provideAudioCoreUseCases(core: SivAnalyzerCore): AudioCoreUseCases {
        return AudioCoreUseCases(
            addChunkUseCase = AddChunkUseCase(core),
            buildAudioBufferUseCase = BuildAudioBufferUseCase(core),
            computeSivUseCase = ComputeSivUseCase(core),
            resetSivMeasurementUseCase = ResetSivMeasurementUseCase(core)
        )
    }

    @Provides
    @Singleton
    fun providePpgCoreUseCases(core: PpgAnalyzerCore): PpgCoreUseCases {
        return PpgCoreUseCases(
            computeBpmUseCase = ComputeBpmUseCase(core),
            collectPpgSignalUseCase = CollectPpgSignalUseCase(core),
            resetBpmMeasurementUseCase = ResetBpmMeasurementUseCase(core)
        )
    }

    @Provides
    @Singleton
    fun provideBreathCoreUseCases(core: BreathAnalyzerCore): BreathCoreUseCases {
        return BreathCoreUseCases(
            computeBrpmUseCase = ComputeBrpmUseCase(core),
            collectZValuesUseCase = CollectZValuesUseCase(core),
            resetBrpmMeasurementUseCase = ResetBrpmMeasurementUseCase(core)
        )
    }
}