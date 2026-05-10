package com.example.taskwhiz.data.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.taskwhiz.di.IoDispatcher
import com.example.taskwhiz.domain.repository.TaskRepository
import com.example.taskwhiz.domain.usecase.reminder.SyncTaskReminderUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DeviceBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var syncTaskReminder: SyncTaskReminderUseCase
    @Inject
    lateinit var taskRepository: TaskRepository


    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    override fun onReceive(context: Context, intent: Intent?) {

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {

            val pendingResult: PendingResult = goAsync() // Tells OS to keep this receiver alive slightly longer
            CoroutineScope(ioDispatcher).launch {
                try {
                    val currentTime = System.currentTimeMillis()
                    val tasksToReschedule =
                        taskRepository.getAllTasksWithFutureReminders(currentTime)

                    tasksToReschedule.forEach { task ->
                        syncTaskReminder(task)
                    }
                } finally {
                    pendingResult.finish()
                }

            }

        }

    }
}
