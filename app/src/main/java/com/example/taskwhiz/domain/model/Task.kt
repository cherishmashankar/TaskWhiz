package com.example.taskwhiz.domain.model

data class Task(
    val id: Long = 0L,
    val title: String = "",                // Empty if only rawInput is available
    val tag: String? = null,
    val createdAt: Long,
    val dueAt: Long? = null,
    val reminderAt: Long? = null,
    val isCompleted: Boolean = false,
    val taskItems: List<String> = emptyList(),
    val colorCode: String = "#FFFFFF",
    val priorityLevel: Int = 2,            // 0 = High, 1 = Medium, 2 = Low
    val lastModifiedAt: Long = createdAt,
    val archived: Boolean = false,
    val isMessy: Boolean = true,           // true if rawInput exists but fields are not AI-parsed
    val rawInput: String? = null           // ✅ Store original user input here
)

enum class Language {
    ENGLISH, GERMAN
}

enum class AppTheme {
    LIGHT, DARK
}