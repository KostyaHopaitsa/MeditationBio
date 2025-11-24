package com.example.meditationbiorefactoring.feature_bio.domain.model

data class MeasurementAnalysis(
    val result: MeasurementResult,
    val progress: Float = 0f
)
