package com.example.meditationbiorefactoring.common

sealed class DomainError {
    data object PermissionDenied : DomainError()
    data object SensorUnavailable : DomainError()
    data object MeasureFailed : DomainError()
    data object Unknown : DomainError()
}

fun DomainError.toUiError(): UiError = when(this) {
    DomainError.SensorUnavailable -> UiError.Sensor
    DomainError.MeasureFailed -> UiError.Measurement
    DomainError.PermissionDenied -> UiError.Permission
    else -> UiError.Unknown
}