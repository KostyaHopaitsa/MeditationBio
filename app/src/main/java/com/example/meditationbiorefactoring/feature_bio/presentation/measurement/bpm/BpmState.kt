package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm

import com.example.meditationbiorefactoring.feature_bio.presentation.util.ErrorType

data class BpmState(
    val isLoading: Boolean = false,
    val isMeasuring: Boolean = false,
    val isMeasured: Boolean = false,
    val value: String = "",
    val status: String = "",
    val error: ErrorType? = null,
    val isTorchEnabled: Boolean = false
)
