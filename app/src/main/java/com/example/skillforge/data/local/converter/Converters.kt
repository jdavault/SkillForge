package com.example.skillforge.data.local.converter

import androidx.room.TypeConverter
import com.example.skillforge.data.model.ActivityType
import com.example.skillforge.data.model.LearningLevel

class Converters {

    // LearningLevel ↔ String
    @TypeConverter
    fun fromLearningLevel(level: LearningLevel): String = level.name

    @TypeConverter
    fun toLearningLevel(value: String): LearningLevel = LearningLevel.valueOf(value)

    // ActivityType ↔ String
    @TypeConverter
    fun fromActivityType(type: ActivityType): String = type.name

    @TypeConverter
    fun toActivityType(value: String): ActivityType = ActivityType.valueOf(value)
}
