package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

class BreathCoreUseCases(
    val computeBrpmUseCase: ComputeBrpmUseCase,
    val collectZValuesUseCase: CollectZValuesUseCase,
    val resetBrpmMeasurementUseCase: ResetBrpmMeasurementUseCase,
)