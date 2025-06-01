package com.example.taskwhiz.data.repository

import com.example.taskwhiz.data.remote.TaskResponse
import com.example.taskwhiz.utils.AiPromptConstants
import com.google.gson.Gson


object AiUtils {

    fun createAiRequest(rawInput: String): Map<String, Any> = mapOf(
        "messages" to listOf(
            mapOf("role" to "system", "content" to AiPromptConstants.SYSTEM_PROMPT),
            mapOf("role" to "user", "content" to rawInput)
        ),
        "model" to "Meta-Llama-3.3-70B-Instruct",
        "max_tokens" to 512,
        "stream" to false
    )

    fun parseAiContent(content: String): TaskResponse? {
        return try {
            Gson().fromJson(content, TaskResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}