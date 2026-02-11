package com.example.meditationbiorefactoring.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.meditationbiorefactoring.music.data.repository.MusicPlayerRepositoryImpl
import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.GetCurrentPositionUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.GetDurationUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.IsPlayingUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.PauseUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.PlayUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.PlayerUseCases
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.ReleasePlayerUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.ResumeUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.SeekToUseCase
import com.example.meditationbiorefactoring.music.domain.use_case.player_use_case.StopUseCase
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
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer =
        ExoPlayer.Builder(context).build()

    @Provides
    @Singleton
    fun provideMusicPlayerRepository(player: ExoPlayer): MusicPlayerRepository =
        MusicPlayerRepositoryImpl(player)


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