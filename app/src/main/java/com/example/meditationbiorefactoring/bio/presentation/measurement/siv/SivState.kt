package com.example.meditationbiorefactoring.bio.presentation.measurement.siv

data class SivState(
    val isLoading: Boolean = false,
    val isMeasuring: Boolean = false,
    val isMeasured: Boolean = false,
    val value: String = "",
    val status: String = "",
    val error: String = ""
)
