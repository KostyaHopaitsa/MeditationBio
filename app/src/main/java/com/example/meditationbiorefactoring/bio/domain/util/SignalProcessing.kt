package com.example.meditationbiorefactoring.bio.domain.util

import kotlin.math.pow
import kotlin.math.sqrt

object SignalProcessing {

    fun movingAverage(data: List<Double>, window: Int = 10): List<Double> {
        if (data.isEmpty()) return emptyList()
        val smooth = mutableListOf<Double>()
        for (i in data.indices) {
            val start = maxOf(0, i - window)
            val end = minOf(data.size - 1, i + window)
            val avg = data.subList(start, end).average()
            smooth.add(avg)
        }
        return smooth
    }

    fun ema(data: DoubleArray, alpha: Double = 0.1): DoubleArray {
        if (data.isEmpty()) return doubleArrayOf()
        val result = DoubleArray(data.size)
        result[0] = data[0]
        for (i in 1 until data.size) {
            result[i] = alpha * data[i] + (1 - alpha) * result[i - 1]
        }
        return result
    }

    fun bandpass(data: DoubleArray): DoubleArray {
        if (data.size < 3) return data
        val result = DoubleArray(data.size)
        for (i in 1 until data.size - 1) {
            result[i] = data[i] - 0.25 * (data[i - 1] + data[i + 1])
        }
        return result
    }

    fun dynamicThreshold(data: DoubleArray, multiplier: Double = 1.0): Double {
        if (data.isEmpty()) return 0.0
        val mean = data.average()
        val std = sqrt(data.map { (it - mean).pow(2) }.average())
        return mean + std * multiplier
    }

    fun normalize(data: DoubleArray): DoubleArray {
        if (data.isEmpty()) return data
        val mean = data.average()
        return data.map { it - mean }.toDoubleArray()
    }
}