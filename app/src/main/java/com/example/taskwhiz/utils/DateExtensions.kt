package com.example.taskwhiz.utils

import android.text.format.DateUtils
import java.util.Calendar

fun isThisWeek(timeInMillis: Long): Boolean {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply { this.timeInMillis = timeInMillis }

    val weekNow = now.get(Calendar.WEEK_OF_YEAR)
    val weekTarget = target.get(Calendar.WEEK_OF_YEAR)
    val yearNow = now.get(Calendar.YEAR)
    val yearTarget = target.get(Calendar.YEAR)

    return weekNow == weekTarget && yearNow == yearTarget
}