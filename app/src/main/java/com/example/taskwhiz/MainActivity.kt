package com.example.taskwhiz


import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.example.taskwhiz.navigation.AppNavGraph
import com.example.taskwhiz.presentation.ui.theme.TaskWhizTheme
import com.example.taskwhiz.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val initialId = intent.getLongExtra(Constants.EXTRA_TASK_ID, -1L)

        setContent {
            TaskWhizTheme {
                val navController = rememberNavController()

                var pendingTaskId by rememberSaveable { mutableLongStateOf(initialId) }

                LaunchedEffect(pendingTaskId) {
                    if (pendingTaskId != -1L) {
                        navController.navigate("task_editor/$pendingTaskId") {
                            launchSingleTop = true
                        }
                        pendingTaskId = -1L
                    }
                }

                DisposableEffect(Unit) {
                    val consumer = Consumer<Intent> { intent ->
                        val id = intent.getLongExtra(Constants.EXTRA_TASK_ID, -1L)
                        if (id != -1L) pendingTaskId = id
                    }
                    addOnNewIntentListener(consumer)
                    onDispose { removeOnNewIntentListener(consumer) }
                }
                AppNavGraph(
                    navController = navController,
                    activity = this@MainActivity
                )
            }
        }
    }
}