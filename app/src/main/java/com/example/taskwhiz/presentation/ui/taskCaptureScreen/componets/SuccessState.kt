package com.example.taskwhiz.presentation.ui.taskCaptureScreen.componets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SuccessState() {
    Text(
        text = "Task created successfully!",
        color = MaterialTheme.colorScheme.primary
    )
}