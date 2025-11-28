package com.example.meditationbiorefactoring.bio.presentation.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toReadableDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return format.format(Date(this))
}