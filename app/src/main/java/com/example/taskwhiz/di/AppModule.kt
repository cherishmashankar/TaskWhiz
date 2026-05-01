package com.example.taskwhiz.di

import android.content.Context
import androidx.room.Room
import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskDatabase
import com.example.taskwhiz.data.preferences.PreferencesManager
import com.example.taskwhiz.data.remote.TaskApiService
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_db"
        )
            .fallbackToDestructiveMigration(false)
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

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

}