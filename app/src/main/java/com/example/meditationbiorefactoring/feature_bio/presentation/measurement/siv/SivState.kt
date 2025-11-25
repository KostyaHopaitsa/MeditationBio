package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.siv

import com.example.meditationbiorefactoring.feature_bio.presentation.util.ErrorType

data class SivState(
    val isLoading: Boolean = false,
    val isMeasuring: Boolean = false,
    val isMeasured: Boolean = false,
    val value: String = "",
    val status: String = "",
    val error: ErrorType? = null
)
