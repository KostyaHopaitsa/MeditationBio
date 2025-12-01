package com.example.meditationbiorefactoring.bio.presentation.measurement.bpm

sealed class BpmEvent {
    data object Start : BpmEvent()
    data object Reset : BpmEvent()
    data class FrameCaptured(val buffer: ByteArray) : BpmEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FrameCaptured

            return buffer.contentEquals(other.buffer)
        }

        override fun hashCode(): Int {
            return buffer.contentHashCode()
        }
    }
    data object NavigateClick : BpmEvent()
}