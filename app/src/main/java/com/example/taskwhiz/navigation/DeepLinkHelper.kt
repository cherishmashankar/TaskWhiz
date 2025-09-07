package com.example.taskwhiz.navigation

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.taskwhiz.MainActivity

object DeepLinkHelper {

    fun getTaskIntent(context: Context, taskId: Long): Intent {
        val uri = "taskwhiz://task/$taskId".toUri()

        return Intent(Intent.ACTION_VIEW, uri, context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}