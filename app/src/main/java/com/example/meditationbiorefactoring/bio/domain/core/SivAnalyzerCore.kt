package com.example.meditationbiorefactoring.bio.domain.core

import com.example.meditationbiorefactoring.bio.domain.util.SignalProcessing
import kotlin.math.pow
import kotlin.math.sqrt

class SivAnalyzerCore {

    private val chunks = mutableListOf<ShortArray>()
    private var totalSize = 0

    fun addChunk(chunk: ShortArray) {
        chunks.add(chunk)
        totalSize += chunk.size
    }

    fun buildBuffer(): ShortArray {
        val result = ShortArray(totalSize)
        var index = 0

        for (chunk in chunks) {
            chunk.copyInto(result, index)
            index += chunk.size
        }

        return result
    }

    fun computeSiv(buffer: ShortArray, length: Int): Double {
        val norm = buffer.take(length).map { it / 32768.0 }.toDoubleArray()
        val normalized = SignalProcessing.normalize(norm)
        val rms = sqrt(normalized.map { it * it }.average())
        val mean = normalized.average()
        val std = sqrt(normalized.map { (it - mean).pow(2) }.average())
        return rms + std
    }

    fun reset() {
        chunks.clear()
        totalSize = 0
    }
}