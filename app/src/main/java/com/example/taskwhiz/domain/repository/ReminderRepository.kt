package com.example.taskwhiz.domain.repository

import com.example.taskwhiz.domain.model.Task

interface ReminderRepository {
    fun scheduleReminder(task: Task)
    fun cancelReminder(taskId: Long)
}