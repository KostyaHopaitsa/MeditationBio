package com.example.meditationbiorefactoring.feature_bio.domain.model

data class SivRawData(
    val buffer: ShortArray,
    val currentIndex: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SivRawData

        if (!buffer.contentEquals(other.buffer)) return false
        if (currentIndex != other.currentIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = buffer.contentHashCode()
        result = 31 * result + currentIndex
        return result
    }
}
