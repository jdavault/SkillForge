package com.example.skillforge.di

import com.example.skillforge.data.repository.ActivityRepository
import com.example.skillforge.data.repository.ActivityRepositoryImpl
import com.example.skillforge.data.repository.FlashcardRepository
import com.example.skillforge.data.repository.FlashcardRepositoryImpl
import com.example.skillforge.data.repository.ProgressRepository
import com.example.skillforge.data.repository.ProgressRepositoryImpl
import com.example.skillforge.data.repository.SkillRepository
import com.example.skillforge.data.repository.SkillRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSkillRepository(impl: SkillRepositoryImpl): SkillRepository

    @Binds
    @Singleton
    abstract fun bindFlashcardRepository(impl: FlashcardRepositoryImpl): FlashcardRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository
}