package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.core.PpgAnalyzerCore
import com.example.meditationbiorefactoring.bio.domain.model.PpgSignalResult
import javax.inject.Inject

class CollectPpgSignalUseCase @Inject constructor(
    private val ppgAnalyzerCore: PpgAnalyzerCore
) {
    operator fun invoke(buffer: ByteArray): PpgSignalResult {
        return ppgAnalyzerCore.collectPpgSignal(buffer)
    }
}