package com.example.meditationbiorefactoring.bio.domain.sensors

interface Accelerometer {
    fun start(onData: (FloatArray) -> Unit)
    fun stop()
}