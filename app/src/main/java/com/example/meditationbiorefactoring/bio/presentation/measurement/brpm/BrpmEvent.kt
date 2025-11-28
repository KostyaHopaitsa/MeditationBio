package com.example.meditationbiorefactoring.bio.presentation.measurement.brpm

sealed class BrpmEvent {
    data object Start : BrpmEvent()
    data object Reset : BrpmEvent()
    data class DataCaptured (val z: Double) : BrpmEvent()
    data object NavigateClick : BrpmEvent()
}