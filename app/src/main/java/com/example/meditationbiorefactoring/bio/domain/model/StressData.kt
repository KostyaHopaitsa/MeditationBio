package com.example.meditationbiorefactoring.bio.domain.model

data class StressData(
    val bpm: Double? = null,
    val brpm: Double? = null,
    val siv: Double? = null,
) {
    fun isComplete(): Boolean = bpm != null && brpm != null && siv != null
}