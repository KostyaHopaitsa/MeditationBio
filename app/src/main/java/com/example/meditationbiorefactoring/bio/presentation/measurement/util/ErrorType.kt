package com.example.meditationbiorefactoring.bio.presentation.measurement.util

sealed class ErrorType {
    data object SensorError : ErrorType()
    data object MeasureError : ErrorType()
    data object UnknownError : ErrorType()
}