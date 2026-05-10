package com.example.taskwhiz.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.taskwhiz.data.reminder.TaskReminderReceiver
import com.example.taskwhiz.domain.repository.ReminderSchedulerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderSchedulerRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ReminderSchedulerRepository {

    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(taskId: Long, title: String, timeInMs: Long) {
        val pendingIntent = createPendingIntent(taskId, title)

        if (canScheduleExact()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMs,
                pendingIntent
            )
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMs,
                pendingIntent
            )
        }
    }

    override fun cancel(taskId: Long) {
        val pendingIntent = createPendingIntent(taskId, "")
        alarmManager.cancel(pendingIntent)
    }

    private fun canScheduleExact(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun createPendingIntent(taskId: Long, title: String): PendingIntent {
        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            putExtra("EXTRA_TASK_ID", taskId)
            putExtra("EXTRA_TASK_TITLE", title)
        }

        return PendingIntent.getBroadcast(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}