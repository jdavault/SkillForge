package com.example.skillforge.di

import android.content.Context
import androidx.room.Room
import com.example.skillforge.data.local.AppDatabase
import com.example.skillforge.data.local.dao.ActivityDao
import com.example.skillforge.data.local.dao.FlashcardDao
import com.example.skillforge.data.local.dao.ProgressDao
import com.example.skillforge.data.local.dao.SkillDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "skillforge_db"
        ).build()
    }

    @Provides
    fun provideSkillDao(database: AppDatabase): SkillDao = database.skillDao()

    @Provides
    fun provideFlashcardDao(database: AppDatabase): FlashcardDao = database.flashcardDao()

    @Provides
    fun provideActivityDao(database: AppDatabase): ActivityDao = database.activityDao()

    @Provides
    fun provideProgressDao(database: AppDatabase): ProgressDao = database.progressDao()
}
