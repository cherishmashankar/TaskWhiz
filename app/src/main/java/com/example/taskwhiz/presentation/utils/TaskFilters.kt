package com.example.taskwhiz.presentation.utils

object TaskFilters {
    const val TODAY = "today"
    const val OVERDUE = "overdue"
    const val REMINDER = "reminder"
    const val HIGH_PRIORITY = "high_priority"

    val DATE_FILTERS = setOf(TODAY, OVERDUE)
}