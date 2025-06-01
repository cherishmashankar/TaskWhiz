package com.example.taskwhiz.data.remote



import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TaskApiService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer YOUR_HUGGINGFACE_API_KEY" // 🔁 Replace at runtime or inject securely
    )
    @POST("sambanova/v1/chat/completions")
    suspend fun getStructuredTask(
        @Body requestBody: Map<String, Any>
    ): TaskApiWrapperResponse
}