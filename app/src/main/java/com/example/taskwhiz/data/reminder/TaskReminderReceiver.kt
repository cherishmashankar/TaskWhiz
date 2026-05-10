package com.example.taskwhiz.data.reminder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.taskwhiz.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TaskReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent?) {

        val taskId = intent?.getLongExtra(Constants.EXTRA_TASK_ID, -1L) ?: -1L
        val taskTitle = intent?.getStringExtra(Constants.EXTRA_TASK_TITLE) ?: Constants.REMINDER_CHANNEL_NAME

        if(taskId != -1L) {
          notificationHelper.showNotification(taskId, taskTitle)
        }

    }
}