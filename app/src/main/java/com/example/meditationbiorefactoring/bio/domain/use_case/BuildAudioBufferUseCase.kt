package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.SivAnalyzerCore
import javax.inject.Inject

class BuildAudioBufferUseCase @Inject constructor(
    private val sivAnalyzerCore: SivAnalyzerCore
) {
    operator fun invoke(): ShortArray {
        return sivAnalyzerCore.buildBuffer()
    }
}