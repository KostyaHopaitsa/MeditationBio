package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.measurement_siv

sealed class SivEvent {
    data object Start : SivEvent()
    data object Stop : SivEvent()
    data object Reset : SivEvent()
    data object NavigateClick : SivEvent()
}