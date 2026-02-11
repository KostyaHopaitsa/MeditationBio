package com.example.meditationbiorefactoring.common

sealed class UiError(val message: String) {
    data object Sensor : UiError("Sensor not available")
    data object Measurement : UiError("Measurement failed")
    data object Permission : UiError("Permission Denied")
    data object Unknown : UiError("Unknown error")
}