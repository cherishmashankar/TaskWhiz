package com.example.taskwhiz.presentation.ui.taskListScreen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FilterItem(
    val key: String,
    val labelRes: Int,
    val count: Int,
    val icon: ImageVector,
    val color: Color
)