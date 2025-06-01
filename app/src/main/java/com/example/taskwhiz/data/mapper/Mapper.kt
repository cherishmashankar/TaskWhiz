package com.example.taskwhiz.data.mapper

import com.example.taskwhiz.data.local.TaskEntity
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
        isMessy = isMessy,
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
        isMessy = isMessy,
        rawInput = rawInput
    )
}
