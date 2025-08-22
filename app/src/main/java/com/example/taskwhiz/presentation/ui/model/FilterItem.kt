package com.example.taskwhiz.presentation.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FilterItem(
    val name: String,
    val count: Int,
    val icon: ImageVector,
    val color: Color
)