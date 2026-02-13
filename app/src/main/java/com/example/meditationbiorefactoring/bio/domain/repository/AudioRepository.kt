package com.example.meditationbiorefactoring.bio.domain.repository

import com.example.meditationbiorefactoring.common.DataResult
import kotlinx.coroutines.flow.Flow

interface AudioRepository {
    fun start(): Flow<DataResult<ShortArray>>
    fun stop()
}