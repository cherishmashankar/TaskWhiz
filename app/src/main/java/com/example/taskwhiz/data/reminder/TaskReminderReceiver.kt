package com.example.taskwhiz.data.reminder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TaskReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent?) {

        val taskId = intent?.getLongExtra("EXTRA_TASK_ID", -1L) ?: -1L
        val taskTitle = intent?.getStringExtra("EXTRA_TASK_TITLE") ?: "Task Reminder"

        if(taskId != -1L) {
          notificationHelper.showNotification(taskId, taskTitle)
        }

    }
}