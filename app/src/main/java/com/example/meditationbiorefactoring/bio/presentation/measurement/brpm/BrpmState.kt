package com.example.meditationbiorefactoring.bio.presentation.measurement.brpm

data class BrpmState(
    val isLoading: Boolean = false,
    val isMeasuring: Boolean = false,
    val isMeasured: Boolean = false,
    val value: String = "",
    val status: String = "",
    val error: String = "",
    val progress: Float = 0f
)
