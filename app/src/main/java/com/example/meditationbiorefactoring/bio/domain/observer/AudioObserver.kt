package com.example.meditationbiorefactoring.bio.domain.observer

interface AudioObserver {
    fun start(onChunk: (ShortArray) -> Unit)
    fun stop()
}