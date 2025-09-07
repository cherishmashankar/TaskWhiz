package com.example.taskwhiz.domain.usecase.reminder

import com.example.taskwhiz.domain.repository.ReminderRepository
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    operator fun invoke(taskId: Long) {
        repository.cancelReminder(taskId)
    }
}