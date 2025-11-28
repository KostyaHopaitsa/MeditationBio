package com.example.meditationbiorefactoring.bio.presentation.measurement.siv

sealed class SivEvent {
    data object Start : SivEvent()
    data object Stop : SivEvent()
    data object Reset : SivEvent()
    data object NavigateClick : SivEvent()
}