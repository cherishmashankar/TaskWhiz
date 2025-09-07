package com.example.taskwhiz.data.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.navigation.DeepLinkHelper

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getLong("taskId", -1L)
        val taskTitle = inputData.getString("taskTitle") ?: return Result.failure()
        val dueDate = inputData.getString("taskDue") // optional
        val priority = inputData.getBoolean("taskPriority", false)

        if (taskId == -1L) return Result.failure()

        showNotification(taskId, taskTitle, dueDate, priority)
        return Result.success()
    }

    private fun showNotification(taskId: Long, title: String, due: String?, priority: Boolean) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channel (Android 8+)
        val channel = NotificationChannel(
            "reminder_channel",
            "context.getString(R.string.reminder_channel_name)",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val intent = DeepLinkHelper.getTaskIntent(context, taskId)
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification) // ✅ add your app icon
            .setContentTitle("⏰ Reminder: $title")
            .setContentText(buildString {
                if (priority) append("⭐ High Priority ")
                due?.let { append("📅 Due $it") }
            }.ifEmpty { "Tap to view task" })
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(taskId.toInt(), notification)
    }
}