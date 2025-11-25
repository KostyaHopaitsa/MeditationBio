package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.brpm

sealed class BrpmEvent {
    data object Start : BrpmEvent()
    data object Reset : BrpmEvent()
    data class DataCaptured (val z: Float) : BrpmEvent()
    data object NavigateClick : BrpmEvent()
}