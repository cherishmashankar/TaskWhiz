package com.example.taskwhiz.domain.model

data class Task(
    val id: Long = 0L,
    val title: String = "",
    val tag: String? = null,
    val createdAt: Long,
    val dueAt: Long? = null,
    val reminderAt: Long? = null,
    val isCompleted: Boolean = false,
    val taskItems: List<String> = emptyList(),
    val colorCode: String = "#FFFFFF",
    val priorityLevel: Int = 2,
    val lastModifiedAt: Long = createdAt,
    val archived: Boolean = false,
    val isMessy: Boolean = true,
    val rawInput: String? = null
)

enum class Language {
    ENGLISH, GERMAN
}

enum class AppTheme {
    LIGHT, DARK
}