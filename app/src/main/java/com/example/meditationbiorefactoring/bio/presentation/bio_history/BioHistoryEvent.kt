package com.example.meditationbiorefactoring.bio.presentation.bio_history

import com.example.meditationbiorefactoring.bio.domain.model.Measurement

sealed class BioHistoryEvent {
    data class Delete(val measurement: Measurement) : BioHistoryEvent()
    data class NavigateClick(val stress: String) : BioHistoryEvent()
}