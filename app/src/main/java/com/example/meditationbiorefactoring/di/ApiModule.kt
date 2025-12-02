package com.example.meditationbiorefactoring.di

import com.example.meditationbiorefactoring.common.Constants
import com.example.meditationbiorefactoring.music.data.remote.JamendoApi
import com.example.meditationbiorefactoring.music.data.repository.TrackRepositoryImpl
import com.example.meditationbiorefactoring.music.domain.repository.TrackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

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
}