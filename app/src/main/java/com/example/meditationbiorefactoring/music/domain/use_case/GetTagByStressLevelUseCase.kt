package com.example.meditationbiorefactoring.music.domain.use_case

import javax.inject.Inject

class GetTagByStressLevelUseCase @Inject constructor() {
    operator fun invoke(stressLevel: String?): String {
        return when (stressLevel) {
            "Low" -> "ambient+chillout"
            "Medium" -> "downtempo"
            "High" -> "calm+meditation"
            else -> "ambient+downtempo+calm"
        }
    }
}