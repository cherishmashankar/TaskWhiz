package com.example.taskwhiz.presentation.sharetask

import android.annotation.SuppressLint
import com.example.taskwhiz.utils.toDateOnly
import com.example.taskwhiz.utils.toFullDateTime
import android.app.Activity
import android.content.Intent

import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task



@SuppressLint("StringFormatInvalid")
fun shareTask(activity: Activity, task: Task) {

    val shareText = buildString {
        appendLine(activity.getString(R.string.share_task_title, task.title))
        appendLine()

        task.dueAt?.let {
            appendLine(activity.getString(R.string.share_task_due, it.toDateOnly()))
        }

        task.reminderAt?.let {
            appendLine(activity.getString(R.string.share_task_reminder, it.toFullDateTime()))
        }
        if (task.priorityLevel == 1) {
            appendLine(
                activity.getString(
                    R.string.share_task_priority_high,
                    activity.getString(R.string.share_task_priority_high_label)
                )
            )
        }
        if (task.taskItems.isNotEmpty()) {
            appendLine()
            appendLine(activity.getString(R.string.share_task_subtasks))
            task.taskItems.forEach {
                appendLine(activity.getString(R.string.share_task_subtask_item, it))
            }
        }
    }.trim()


    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.share_task))
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    val chooser = Intent.createChooser(sendIntent, activity.getString(R.string.share_task))
    activity.startActivity(chooser)
}

