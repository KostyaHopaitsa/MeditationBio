package com.example.meditationbiorefactoring.common

import android.database.sqlite.SQLiteException
import androidx.media3.common.PlaybackException
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

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
}

fun Throwable.toDomainError(): DomainError = when (this) {
    is SecurityException -> DomainError.PermissionDenied
    is IllegalStateException -> DomainError.SensorUnavailable
    is PlaybackException -> DomainError.PlaybackError
    is IOException -> DomainError.NetworkError
    is HttpException -> DomainError.HttpError(code = this.code())
    is JsonSyntaxException -> DomainError.ParsingError
    is SQLiteException -> DomainError.DatabaseError
    else -> DomainError.Unknown
}