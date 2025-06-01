package com.example.taskwhiz.data.local


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "tag")
    val tag: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "due_at")
    val dueAt: Long? = null,

    @ColumnInfo(name = "reminder_at")
    val reminderAt: Long? = null,

    @ColumnInfo(name = "is_messy")
    val isMessy: Boolean = true,

    @ColumnInfo(name = "raw_input")
    val rawInput: String? = null, // Original messy input if any

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "task_items")
    val taskItems: List<String> = emptyList(), // Requires a TypeConverter

    @ColumnInfo(name = "color_code")
    val colorCode: String = "#FFFFFF",

    @ColumnInfo(name = "priority_level")
    val priorityLevel: Int = 2, // 0 = High, 1 = Medium, 2 = Low

    @ColumnInfo(name = "last_modified_at")
    val lastModifiedAt: Long = createdAt,

    @ColumnInfo(name = "archived")
    val archived: Boolean = false
)