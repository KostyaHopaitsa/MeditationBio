package com.example.meditationbiorefactoring.bio.domain.repository

import kotlinx.coroutines.flow.Flow

interface AudioRepository {
    fun start(): Flow<ShortArray>
    fun stop()
}