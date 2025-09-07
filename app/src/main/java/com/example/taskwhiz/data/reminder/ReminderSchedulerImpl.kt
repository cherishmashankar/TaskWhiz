package com.example.taskwhiz.data.reminder

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.ReminderRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderRepository {

    override fun scheduleReminder(task: Task) {
        val dueTime = task.reminderAt ?: return

        val delay = dueTime - System.currentTimeMillis()
        if (delay <= 0) return

        val data = workDataOf("taskId" to task.id)

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("reminder_${task.id}")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "reminder_${task.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun cancelReminder(taskId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("reminder_$taskId")
    }
}