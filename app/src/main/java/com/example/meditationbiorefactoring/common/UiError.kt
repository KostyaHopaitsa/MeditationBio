package com.example.meditationbiorefactoring.common

sealed class UiError(val message: String) {
    object Permission : UiError("Permission Denied")
    object Sensor : UiError("Sensor not available")
    object Playback : UiError("Playback failed")
    object Network : UiError("Network error")
    data class Http(val code: Int) : UiError("HTTP error code: $code")
    object Parsing : UiError("Parsing error")
    object Database : UiError("Database error")
    object Unknown : UiError("Unknown error")
    object Measurement : UiError("Measurement failed")
}

fun DomainError.toUiError(): UiError = when (this) {
    DomainError.PermissionDenied -> UiError.Permission
    DomainError.SensorUnavailable -> UiError.Sensor
    DomainError.PlaybackError -> UiError.Playback
    DomainError.NetworkError -> UiError.Network
    is DomainError.HttpError -> UiError.Http(code)
    DomainError.ParsingError -> UiError.Parsing
    DomainError.DatabaseError -> UiError.Database
    DomainError.MeasureFailed -> UiError.Measurement
    DomainError.Unknown -> UiError.Unknown
}