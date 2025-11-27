package com.example.meditationbiorefactoring.feature_bio.presentation.measurement

import com.example.meditationbiorefactoring.feature_bio.domain.model.StressData

data class MeasurementState(
    val stressData: StressData = StressData(),
    val overallStress: String = ""
)

