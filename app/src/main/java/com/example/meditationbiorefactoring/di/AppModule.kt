package com.example.meditationbiorefactoring.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.meditationbiorefactoring.common.Constants
import com.example.meditationbiorefactoring.bio.data.local.MeasurementDatabase
import com.example.meditationbiorefactoring.bio.data.repository.BpmRepositoryImpl
import com.example.meditationbiorefactoring.bio.data.repository.BrpmRepositoryImpl
import com.example.meditationbiorefactoring.bio.data.repository.MeasurementRepositoryImpl
import com.example.meditationbiorefactoring.bio.data.repository.SivRepositoryImpl
import com.example.meditationbiorefactoring.bio.domain.repository.BpmRepository
import com.example.meditationbiorefactoring.bio.domain.repository.BrpmRepository
import com.example.meditationbiorefactoring.bio.domain.repository.MeasurementRepository
import com.example.meditationbiorefactoring.bio.domain.repository.SivRepository
import com.example.meditationbiorefactoring.bio.data.analyzer.BreathAnalyzerCore
import com.example.meditationbiorefactoring.bio.data.analyzer.PpgAnalyzerCore
import com.example.meditationbiorefactoring.bio.data.analyzer.SivAnalyzerCore
import com.example.meditationbiorefactoring.music.data.remote.JamendoApi
import com.example.meditationbiorefactoring.music.data.repository.MusicPlayerRepositoryImpl
import com.example.meditationbiorefactoring.music.data.repository.TrackRepositoryImpl
import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository
import com.example.meditationbiorefactoring.music.domain.repository.TrackRepository
import com.example.meditationbiorefactoring.music.domain.use_case.GetCurrentPositionUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.GetDurationUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.IsPlayingUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.PauseUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.PlayUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.PlayerUseCases
import com.example.meditationbiorefactoring.music.domain.use_case.ReleasePlayerUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.ResumeUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.SeekToUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.StopUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideBpmRepository(analyzer: PpgAnalyzerCore): BpmRepository {
        return BpmRepositoryImpl(analyzer)
    }

    @Provides
    @Singleton
    fun provideBrpmRepository(analyzer: BreathAnalyzerCore): BrpmRepository {
        return BrpmRepositoryImpl(analyzer)
    }

    @Provides
    @Singleton
    fun provideSivRepository(analyzer: SivAnalyzerCore): SivRepository {
        return SivRepositoryImpl(analyzer)
    }

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

    @Provides
    @Singleton
    fun provideMusicPlayerRepository(
        @ApplicationContext context: Context
    ): MusicPlayerRepository {
        return MusicPlayerRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideJamendoApi(): JamendoApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JamendoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTrackRepository(api: JamendoApi): TrackRepository {
        return TrackRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePlayerUseCases(repository: MusicPlayerRepository): PlayerUseCases {
        return PlayerUseCases(
            playUseCase = PlayUseCase(repository),
            pauseUseCase = PauseUseCase(repository),
            resumeUseCase = ResumeUseCase(repository),
            getCurrentPositionUseCase = GetCurrentPositionUseCase(repository),
            getDurationUseCase = GetDurationUseCase(repository),
            isPlayingUseCase = IsPlayingUseCase(repository),
            seekToUseCase = SeekToUseCase(repository),
            stopUseCase = StopUseCase(repository),
            releasePlayerUseCase = ReleasePlayerUseCase(repository)
        )
    }
}