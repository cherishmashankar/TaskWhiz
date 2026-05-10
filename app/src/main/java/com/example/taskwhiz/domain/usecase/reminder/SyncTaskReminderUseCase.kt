package com.example.taskwhiz.domain.usecase.reminder

import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.ReminderSchedulerRepository
import javax.inject.Inject

class SyncTaskReminderUseCase @Inject constructor(
    private val reminderSchedulerRepository: ReminderSchedulerRepository

){
    operator fun invoke(task: Task) {
        if (task.reminderAt != null && task.reminderAt > System.currentTimeMillis() ) {
            reminderSchedulerRepository.schedule(task.id, task.title, task.reminderAt)
        } else {
            reminderSchedulerRepository.cancel(task.id)
        }
    }
}

