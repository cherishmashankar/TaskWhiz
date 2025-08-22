package com.example.taskwhiz.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskDatabase
import com.example.taskwhiz.data.preferences.PreferencesManager
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.repository.PreferencesRepositoryImpl

import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.repository.PreferencesRepository
import com.example.taskwhiz.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Room Database
    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao = db.taskDao()

    // Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://router.huggingface.co/sambanova/") // Replace with your API base
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideTaskApiService(retrofit: Retrofit): TaskApiService =
        retrofit.create(TaskApiService::class.java)

    // Repository
    @Provides
    @Singleton
    fun provideTaskRepository(
        dao: TaskDao,
        api: TaskApiService
    ): TaskRepository = TaskRepositoryImpl(dao, api)

    // Preferences
    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(
        manager: PreferencesManager
    ): PreferencesRepository {
        return PreferencesRepositoryImpl(manager)
    }
}