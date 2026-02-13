package com.example.meditationbiorefactoring.common

sealed class DomainError {
    object PermissionDenied : DomainError()
    object SensorUnavailable : DomainError()
    data object PlaybackError : DomainError()
    data object NetworkError : DomainError()
    data class HttpError(val code: Int) : DomainError()
    data object ParsingError : DomainError()
    data object DatabaseError : DomainError()
    object Unknown : DomainError()
    data object MeasureFailed : DomainError()
    data object NotComplete : DomainError()
}
