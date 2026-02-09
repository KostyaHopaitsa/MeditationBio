package com.example.meditationbiorefactoring.bio.domain.observer

interface AccelerometerObserver {
    fun start(onData: (FloatArray) -> Unit)
    fun stop()
}