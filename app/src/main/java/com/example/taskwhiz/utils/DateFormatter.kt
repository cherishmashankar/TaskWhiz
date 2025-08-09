package com.example.taskwhiz.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FULL_DATE_TIME_FORMATTER =
    DateTimeFormatter.ofPattern("MMM dd, h:mm a")

private val DATE_ONLY_FORMATTER =
    DateTimeFormatter.ofPattern("MMM dd")

fun Long.toFullDateTime(locale: Locale = Locale.getDefault()): String {
    val zoned = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    return FULL_DATE_TIME_FORMATTER.withLocale(locale).format(zoned)
}

fun Long.toDateOnly(locale: Locale = Locale.getDefault()): String {
    val zoned = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    return DATE_ONLY_FORMATTER.withLocale(locale).format(zoned)
}