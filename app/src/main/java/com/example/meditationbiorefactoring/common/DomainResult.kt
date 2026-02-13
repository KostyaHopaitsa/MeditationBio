package com.example.meditationbiorefactoring.common

sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val error: DomainError) : DomainResult<Nothing>()
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
    DomainError.NotComplete -> UiError.Collection
    DomainError.Unknown -> UiError.Unknown
}

