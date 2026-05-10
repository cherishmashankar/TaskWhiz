package com.example.taskwhiz.domain.repository

interface ReminderSchedulerRepository {
    fun schedule(taskId: Long, title: String, timeInMs: Long)
    fun cancel(taskId: Long)
}