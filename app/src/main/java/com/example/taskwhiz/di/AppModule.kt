package com.example.taskwhiz.di


import android.app.Application
import androidx.room.Room
import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskDatabase
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase =
        Room.databaseBuilder(app, TaskDatabase::class.java, "tasks_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://router.huggingface.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideTaskApiService(retrofit: Retrofit): TaskApiService =
        retrofit.create(TaskApiService::class.java)

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao,
        taskApiService: TaskApiService
    ): TaskRepository = TaskRepository(taskDao, taskApiService)
}
