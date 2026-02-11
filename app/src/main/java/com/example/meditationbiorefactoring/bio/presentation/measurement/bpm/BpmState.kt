package com.example.meditationbiorefactoring.bio.presentation.measurement.bpm

data class BpmState(
    val isLoading: Boolean = false,
    val isMeasuring: Boolean = false,
    val isMeasured: Boolean = false,
    val value: String = "",
    val status: String = "",
    val error: String = "",
    val isTorchEnabled: Boolean = false,
    val progress: Float = 0f
)
