package com.example.taskwhiz.domain.usecase.reminder

import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.ReminderRepository

import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    operator fun invoke(task: Task) {
        repository.scheduleReminder(task)
    }
}