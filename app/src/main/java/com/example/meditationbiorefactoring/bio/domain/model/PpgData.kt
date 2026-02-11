package com.example.meditationbiorefactoring.bio.domain.model

data class PpgData(
    val values: List<Double>,
    val timestamps: List<Long>,
    val progress: Float
)
