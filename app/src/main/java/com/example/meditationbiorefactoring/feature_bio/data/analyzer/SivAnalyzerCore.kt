package com.example.meditationbiorefactoring.feature_bio.data.analyzer

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.example.meditationbiorefactoring.feature_bio.domain.model.SivRawData
import com.example.meditationbiorefactoring.feature_bio.domain.util.SignalProcessing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class SivAnalyzerCore {

    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private var recorder: AudioRecord? = null
    private lateinit var buffer: ShortArray
    private var bufferSize = 0
    private var currentIndex = 0
    private var recordingJob: Job? = null
    private var isRecording = false

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun startRecording() {
        if (isRecording) return

        try {
            bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            if (bufferSize <= 0) return

            buffer = ShortArray(bufferSize * 5)

            recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            ).apply {
                if (state != AudioRecord.STATE_INITIALIZED) return
                startRecording()
            }

            isRecording = true
            currentIndex = 0

            recordingJob = scope.launch {
                while (isRecording && currentIndex < buffer.size) {
                    val read = recorder?.read(buffer, currentIndex, buffer.size - currentIndex) ?: 0
                    if (read > 0) currentIndex += read
                    delay(5)
                }
            }

        } catch (_: SecurityException) {
            stopRecording()
        }
    }

    fun stopRecording(){
        isRecording = false
        recordingJob?.cancel()
        recordingJob = null

        try {
            recorder?.stop()
        } catch (_: IllegalStateException) {}
        recorder?.release()
        recorder = null
    }

    fun getRawData(): SivRawData {
        if (!::buffer.isInitialized || currentIndex <= 0)
            return SivRawData(ShortArray(0) , 0)

        return SivRawData(buffer.copyOf(), currentIndex)
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
        stopRecording()
        currentIndex = 0
        buffer = ShortArray(0)
    }
}