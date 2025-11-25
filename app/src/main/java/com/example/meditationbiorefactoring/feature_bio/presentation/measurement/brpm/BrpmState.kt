package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.brpm

import com.example.meditationbiorefactoring.feature_bio.presentation.util.ErrorType

data class BrpmState(
    val isLoading: Boolean = false,
    val isMeasuring: Boolean = false,
    val isMeasured: Boolean = false,
    val value: String = "",
    val status: String = "",
    val error: ErrorType? = null
)
