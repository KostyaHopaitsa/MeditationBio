package com.example.meditationbiorefactoring.bio.presentation.measurement

import com.example.meditationbiorefactoring.bio.domain.model.StressData

data class MeasurementState(
    val stressData: StressData = StressData(),
    val overallStress: String = ""
)

