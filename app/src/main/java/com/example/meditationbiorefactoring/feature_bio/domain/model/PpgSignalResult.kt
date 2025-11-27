package com.example.meditationbiorefactoring.feature_bio.domain.model

data class PpgSignalResult(
    val values: List<Double>,
    val timestamps: List<Long>,
    val progress: Float
)
