package com.example.meditationbiorefactoring.bio.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Measurement(
    val timestamp: Long,
    val bpm: Double,
    val brpm: Double,
    val siv: Double,
    val stress: String,
    @PrimaryKey val id: Int? = null
)
