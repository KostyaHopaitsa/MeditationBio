package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.measurement_bpm

sealed class BpmEvent {
    data object Start : BpmEvent()
    data object Reset : BpmEvent()
    data class FrameCaptured(val buffer: ByteArray) : BpmEvent()
    data object NavigateClick : BpmEvent()
}