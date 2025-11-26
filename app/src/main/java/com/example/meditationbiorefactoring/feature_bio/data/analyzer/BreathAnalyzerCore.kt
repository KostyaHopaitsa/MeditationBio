package com.example.meditationbiorefactoring.feature_bio.data.analyzer

import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.feature_bio.domain.util.SignalProcessing
import javax.inject.Inject
import kotlin.math.min

class BreathAnalyzerCore @Inject constructor() {
    private val zValues = mutableListOf<Float>()
    private val bufferSize = 1200
    private val minPeakDistance = 30
    private val minPeakAmplitude = 0.007f

    fun processFrame(z: Float): MeasurementAnalysis {
        zValues.add(z)
        if (zValues.size > bufferSize) zValues.removeAt(0)

        val progress = zValues.size.toFloat() / bufferSize.toFloat()

        return if (zValues.size == bufferSize) {
            val brpm = computeBrpm(zValues)
            if (brpm in 5..40) {
                MeasurementAnalysis(MeasurementResult.Success(brpm.toDouble()), progress)
            } else {
                MeasurementAnalysis(MeasurementResult.Invalid(brpm.toDouble()), progress)
            }
        } else {
            MeasurementAnalysis(MeasurementResult.Error, progress)
        }
    }

    fun reset() {
        zValues.clear()
    }

    private fun computeBrpm(values: List<Float>): Int {
        val smooth = SignalProcessing.movingAverage(values, window = 20)
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

        return (peaks * 60.0 / (bufferSize / 30.0)).toInt()
    }
}