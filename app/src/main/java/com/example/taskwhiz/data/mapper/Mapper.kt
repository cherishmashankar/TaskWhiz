package com.example.taskwhiz.data.mapper

import com.example.taskwhiz.data.local.TaskEntity
import com.example.taskwhiz.data.remote.dto.TaskResponseDto
import com.example.taskwhiz.domain.model.Task



fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        tag = tag,
        createdAt = createdAt,
        dueAt = dueAt,
        reminderAt = reminderAt,
        isCompleted = isCompleted,
        taskItems = taskItems,
        colorCode = colorCode,
        priorityLevel = priorityLevel,
        lastModifiedAt = lastModifiedAt,
        archived = archived,
        isAIGenerated = isAIGenerated,
        rawInput = rawInput
    )
}
fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        tag = tag,
        createdAt = createdAt,
        dueAt = dueAt,
        reminderAt = reminderAt,
        isCompleted = isCompleted,
        taskItems = taskItems,
        colorCode = colorCode,
        priorityLevel = priorityLevel,
        lastModifiedAt = lastModifiedAt,
        archived = archived,
        isAIGenerated = isAIGenerated,
        rawInput = rawInput
    )
}

fun TaskResponseDto.toDomain(): Task {
    return Task(
        id = 0L,
        title = title,
        tag = tag,
        createdAt = System.currentTimeMillis(),
        dueAt = dueAt,
        reminderAt = reminderAt,
        isCompleted = false,
        taskItems = taskItems ?: emptyList(),
        colorCode = "#FFFFFF",
        priorityLevel = priorityLevel,
        lastModifiedAt = System.currentTimeMillis(),
        archived = false,
        isAIGenerated = true,
        rawInput = rawInput
    )
}