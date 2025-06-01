package com.example.taskwhiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskwhiz.presentation.ui.screen.TaskListScreen
import com.example.taskwhiz.presentation.ui.theme.TaskWhizTheme
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskWhizTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: TaskViewModel = hiltViewModel()
                    TaskListScreen(viewModel)
                }
            }
        }
    }
}