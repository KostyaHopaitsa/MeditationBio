package com.example.meditationbiorefactoring.di

import android.content.Context
import com.example.meditationbiorefactoring.music.data.repository.MusicPlayerRepositoryImpl
import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository
import com.example.meditationbiorefactoring.music.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {

    @Provides
    @Singleton
    fun provideMusicPlayerRepository(
        @ApplicationContext context: Context
    ): MusicPlayerRepository {
        return MusicPlayerRepositoryImpl(context)
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