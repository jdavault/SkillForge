package com.example.skillforge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.skillforge.data.local.converter.Converters
import com.example.skillforge.data.local.dao.ActivityDao
import com.example.skillforge.data.local.dao.FlashcardDao
import com.example.skillforge.data.local.dao.ProgressDao
import com.example.skillforge.data.local.dao.SkillDao
import com.example.skillforge.data.model.Activity
import com.example.skillforge.data.model.Flashcard
import com.example.skillforge.data.model.Skill
import com.example.skillforge.data.model.UserProgress

@Database(
    entities = [
        Skill::class,
        Flashcard::class,
        Activity::class,
        UserProgress::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun skillDao(): SkillDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun activityDao(): ActivityDao
    abstract fun progressDao(): ProgressDao
}
