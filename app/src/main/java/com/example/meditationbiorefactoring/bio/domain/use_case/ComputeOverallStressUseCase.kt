package com.example.meditationbiorefactoring.bio.domain.use_case

import com.example.meditationbiorefactoring.bio.domain.model.StressData
import javax.inject.Inject

class ComputeOverallStressUseCase @Inject constructor() {
    operator fun invoke(data: StressData): String {
        val bpm = data.bpm
        val brpm = data.brpm
        val siv = data.siv

        if (bpm == null || brpm == null || siv == null) return "Unknown"

        var score = 0

        score += when {
            bpm > 90 -> 2
            bpm > 75 -> 1
            else -> 0
        }

        score += when {
            brpm > 20 -> 2
            brpm > 15 -> 1
            else -> 0
        }

        score += when {
            siv > 0.09 -> 2
            siv > 0.03 -> 1
            else -> 0
        }

        return when {
            score <= 1 -> "Low"
            score <= 3 -> "Medium"
            else -> "High"
        }
    }
}