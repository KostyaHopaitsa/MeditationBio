package com.example.meditationbiorefactoring.bio.data.controller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.meditationbiorefactoring.bio.domain.sensors.Accelerometer

class AccelerometerController(
    private val context: Context
) : Accelerometer {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var callback: ((FloatArray) -> Unit)? = null

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            callback?.invoke(event.values.copyOf())
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun start(onData: (FloatArray) -> Unit) {
        callback = onData
        accelerometer?.let {
            sensorManager.registerListener(
                listener,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun stop() {
        sensorManager.unregisterListener(listener)
        callback = null
    }
}