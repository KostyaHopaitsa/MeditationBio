package com.example.meditationbiorefactoring.bio.domain.core

import com.example.meditationbiorefactoring.bio.domain.model.ZSignalResult
import com.example.meditationbiorefactoring.bio.domain.util.SignalProcessing
import javax.inject.Inject
import kotlin.math.min

class BreathAnalyzerCore @Inject constructor() {
    private val zValues = mutableListOf<Double>()
    private val bufferSize = 1200
    private val minPeakDistance = 30
    private val minPeakAmplitude = 0.007f

    fun collectZValues(z: Double): ZSignalResult {
        zValues.add(z)
        if (zValues.size > bufferSize) zValues.removeAt(0)

        val progress = zValues.size.toFloat() / bufferSize.toFloat()

        return ZSignalResult(zValues, progress)
    }

    fun computeBrpm(values: List<Double>): Double {
        val smooth = SignalProcessing.movingAverage(data = values, window = 20)
        var peaks = 0
        var lastPeak = -minPeakDistance
        for (i in 1 until smooth.size - 1) {
            val current = smooth[i]
            val prev = smooth[i - 1]
            val next = smooth[i + 1]

            if (current > prev && current > next) {
                if ((i - lastPeak) > minPeakDistance) {
                    val localMin = min(prev, next)
                    val amplitude = current - localMin
                    if (amplitude >= minPeakAmplitude) {
                        peaks++
                        lastPeak = i
                    }
                }
            }
        }

        return (peaks * 60.0 / (bufferSize / 30.0))
    }

    fun reset() {
        zValues.clear()
    }
}