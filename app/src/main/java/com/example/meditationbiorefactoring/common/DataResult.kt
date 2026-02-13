package com.example.meditationbiorefactoring.common

import android.database.sqlite.SQLiteException
import androidx.media3.common.PlaybackException
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error(val throwable: Throwable) : DataResult<Nothing>()
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