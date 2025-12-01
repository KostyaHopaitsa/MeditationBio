package com.example.meditationbiorefactoring.bio.domain.use_case

class AudioCoreUseCases(
    val addChunkUseCase: AddChunkUseCase,
    val buildAudioBufferUseCase: BuildAudioBufferUseCase,
    val computeSivUseCase: ComputeSivUseCase,
    val resetSivMeasurementUseCase: ResetSivMeasurementUseCase
)