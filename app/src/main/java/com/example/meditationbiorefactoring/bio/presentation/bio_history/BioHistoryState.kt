package com.example.meditationbiorefactoring.bio.presentation.bio_history

import com.example.meditationbiorefactoring.bio.domain.model.Measurement

data class BioHistoryState(
    val measurements: List<Measurement> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
