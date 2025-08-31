package com.example.taskwhiz.data.remote

import com.google.gson.annotations.SerializedName

// Top-level API response structure
data class TaskApiWrapperResponse(
    @SerializedName("choices")
    val choices: List<Choice>
)

data class Choice(
    @SerializedName("message")
    val message: Message
)

data class Message(
    @SerializedName("content")
    val content: String // This is a JSON string that needs to be parsed separately
)


data class TaskResponse(
    val title: String,
    val dueAt: Long?,
    val priorityLevel: Int?,
    val tag: String?,
    val taskItems: List<String>,
    val reminderAt: Long?
)