package com.example.meditationbiorefactoring.feature_bio.data.analyzer

import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.feature_bio.domain.util.SignalProcessing
import javax.inject.Inject

class PpgAnalyzerCore @Inject constructor() {

    private val values = mutableListOf<Double>()
    private val timestamps = mutableListOf<Long>()
    private val maxBufferSize = 200
    private var done = false
    private var lastResult: MeasurementAnalysis? = null

    fun analyzeFrame(buffer: ByteArray): MeasurementAnalysis {
        if (done) return lastResult ?: MeasurementAnalysis(MeasurementResult.Error, 1f)

        val avg = buffer.map { it.toInt() and 0xFF }.average()
        values.add(avg)
        timestamps.add(System.currentTimeMillis())

        if (values.size > maxBufferSize) {
            values.removeAt(0)
            timestamps.removeAt(0)
        }

        val progress = values.size / maxBufferSize.toFloat()

        return if (values.size == maxBufferSize) {
            val bpm = computeBpm(values, timestamps)
            done = true
            lastResult = if (bpm in 40..150) {
                MeasurementAnalysis(MeasurementResult.Success(bpm.toDouble()), 1f)
            } else {
                MeasurementAnalysis(MeasurementResult.Invalid, 1f)
            }
            lastResult!!
        } else {
            MeasurementAnalysis(MeasurementResult.Error, progress)
        }
    }

    fun reset() {
        values.clear()
        timestamps.clear()
        done = false
        lastResult = null
    }

    private fun computeBpm(signal: List<Double>, times: List<Long>): Int {
        val smoothed = SignalProcessing.ema(signal.toDoubleArray(), alpha = 0.5)
        val normalized = SignalProcessing.normalize(smoothed)
        val filtered = SignalProcessing.bandpass(normalized)
        val threshold = SignalProcessing.dynamicThreshold(filtered, multiplier = 0.1)

        var peaks = 0
        val minPeakDistance = 3
        var i = minPeakDistance

        while (i < filtered.size - minPeakDistance) {
            var isPeak = filtered[i] > threshold
            for (j in 1..minPeakDistance) {
                if (filtered[i] <= filtered[i - j] || filtered[i] <= filtered[i + j]) {
                    isPeak = false
                    break
                }
            }
            if (isPeak) {
                peaks++
                i += minPeakDistance
            } else {
                i++
            }
        }

        val durationSec = (times.last() - times.first()) / 1000.0
        val bpm = (peaks * 60 / durationSec).toInt()
        return bpm
    }
}