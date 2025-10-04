package com.example.taskwhiz.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskDatabase
import com.example.taskwhiz.data.preferences.PreferencesManager
import com.example.taskwhiz.data.reminder.ReminderRepositoryImpl
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.repository.PreferencesRepositoryImpl

import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.repository.PreferencesRepository
import com.example.taskwhiz.domain.repository.ReminderRepository
import com.example.taskwhiz.domain.repository.TaskRepository
import com.example.taskwhiz.domain.usecase.reminder.CancelReminderUseCase
import com.example.taskwhiz.domain.usecase.reminder.ScheduleReminderUseCase
import dagger.Binds
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
    fun provideRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl("https://router.huggingface.co/sambanova/")
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

    @Provides
    @Singleton
    fun provideReminderRepository(
        @ApplicationContext context: Context
    ): ReminderRepository {
        return ReminderRepositoryImpl(
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideScheduleReminderUseCase(
        repository: ReminderRepository
    ): ScheduleReminderUseCase = ScheduleReminderUseCase(repository)

    @Provides
    @Singleton
    fun provideCancelReminderUseCase(
        repository: ReminderRepository
    ): CancelReminderUseCase = CancelReminderUseCase(repository)
}