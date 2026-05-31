package com.example.taskwhiz.data.remote.dto

data class TaskResponseDto(
    val title: String,
    val dueAt: Long?,
    val reminderAt: Long?,
    val priorityLevel: Int,
    val taskItems: List<String>,
    val tag: String?,
    val rawInput: String
)