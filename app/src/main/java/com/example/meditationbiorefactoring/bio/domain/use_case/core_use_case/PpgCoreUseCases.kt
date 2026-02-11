package com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case

class PpgCoreUseCases(
    val computeBpmUseCase: ComputeBpmUseCase,
    val collectPpgSignalUseCase: CollectPpgSignalUseCase,
    val resetBpmMeasurementUseCase: ResetBpmMeasurementUseCase,
)