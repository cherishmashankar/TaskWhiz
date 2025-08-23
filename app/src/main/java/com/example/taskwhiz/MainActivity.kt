package com.example.taskwhiz

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.navigation.AppNavGraph
import com.example.taskwhiz.presentation.ui.theme.TaskWhizTheme
import com.example.taskwhiz.presentation.util.updateLocale
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: TaskViewModel = hiltViewModel()
            val theme by viewModel.theme.collectAsState()
            val language by viewModel.language.collectAsState()

            val context = LocalContext.current
            val localizedContext = remember(language) {
                context.updateLocale(language)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext
            ) {
                TaskWhizTheme(darkTheme = theme == AppTheme.DARK) {
                    val navController = rememberNavController()

                    // Use your navigation graph
                    AppNavGraph(
                        navController = navController,
                        onSaveTask = { task -> viewModel.addNewTask(task) },
                        onUpdateTask = { task -> viewModel.updateExistingTask(task) },
                        taskViewModel = viewModel

                    )
                }
            }
        }
    }
}