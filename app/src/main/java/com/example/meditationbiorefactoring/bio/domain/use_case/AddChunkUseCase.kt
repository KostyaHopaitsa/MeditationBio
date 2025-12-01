package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.SivAnalyzerCore
import javax.inject.Inject

class AddChunkUseCase @Inject constructor(
    private val sivAnalyzerCore: SivAnalyzerCore
) {
    operator fun invoke(chunk: ShortArray) {
        return sivAnalyzerCore.addChunk(chunk = chunk)
    }
}