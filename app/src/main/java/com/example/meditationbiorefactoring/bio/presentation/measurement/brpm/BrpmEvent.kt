package com.example.meditationbiorefactoring.bio.presentation.measurement.brpm

import com.example.meditationbiorefactoring.bio.domain.model.ZData

sealed class BrpmEvent {
    data object Start : BrpmEvent()
    data object Reset : BrpmEvent()
    data class DataCaptured (val z: ZData) : BrpmEvent()
    data object NavigateClick : BrpmEvent()
}