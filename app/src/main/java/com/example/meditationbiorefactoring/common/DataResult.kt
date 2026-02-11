package com.example.meditationbiorefactoring.common

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error(val throwable: Throwable) : DataResult<Nothing>()
}

fun Throwable.toDomainError(): DomainError = when (this) {
    is SecurityException -> DomainError.PermissionDenied
    is IllegalStateException -> DomainError.SensorUnavailable
    else -> DomainError.Unknown
}