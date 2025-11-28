package com.example.meditationbiorefactoring.music.domain.repository

interface MusicPlayerRepository {
    fun play(url: String)
    fun pause()
    fun resume()
    fun stop()
    fun release()
    fun seekTo(positionMs: Long)
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun isPlaying(): Boolean
}