package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.util

sealed class ErrorType {
    data object SensorError : ErrorType()
    data object MeasureError : ErrorType()
    data object UnknownError : ErrorType()
}