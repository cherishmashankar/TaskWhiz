package com.example.taskwhiz


import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.navigation.AppNavGraph
import com.example.taskwhiz.presentation.ui.theme.TaskWhizTheme
import com.example.taskwhiz.presentation.utils.updateLocale
import com.example.taskwhiz.presentation.viewmodel.SettingsViewModel
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var notificationTaskId = mutableLongStateOf(-1L)
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        notificationTaskId.longValue = intent.getLongExtra("EXTRA_TASK_ID", -1L)

        setContent {
            val activity = this
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val theme by settingsViewModel.theme.collectAsState()
//            val language by settingsViewModel.language.collectAsState()

//            val context = LocalContext.current
//            val localizedContext = remember(language) {
//                context.updateLocale(language)
//            }

                TaskWhizTheme() {
                    val navController = rememberNavController()
                    val taskId = notificationTaskId.longValue
                    LaunchedEffect(taskId) {
                        if (taskId != -1L) {
                            navController.navigate("task_editor/$taskId")
                            notificationTaskId.longValue = -1L
                        }
                    }

                    AppNavGraph(
                        navController = navController,
                        activity = activity
                    )
                }

        }
                //throw RuntimeException("Test Crash: Firebase Crashlytics is working!")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val taskId = intent.getLongExtra("EXTRA_TASK_ID", -1L)

        if (taskId != -1L) {
            notificationTaskId.longValue = taskId
        }
    }
}