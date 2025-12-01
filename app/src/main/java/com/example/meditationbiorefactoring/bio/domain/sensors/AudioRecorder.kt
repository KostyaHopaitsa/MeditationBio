package com.example.meditationbiorefactoring.bio.domain.sensors

interface AudioRecorder {
    fun start(onChunk: (ShortArray) -> Unit)
    fun stop()
}