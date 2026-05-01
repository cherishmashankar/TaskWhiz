package com.example.taskwhiz.di

import com.example.taskwhiz.data.repository.PreferencesRepositoryImpl
import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.repository.PreferencesRepository
import com.example.taskwhiz.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: PreferencesRepositoryImpl
    ): PreferencesRepository

}