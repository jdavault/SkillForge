package com.example.skillforge.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_progress",
    foreignKeys = [
        ForeignKey(
            entity = Skill::class,
            parentColumns = ["id"],
            childColumns = ["skillId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("skillId", unique = true)]
)
data class UserProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val skillId: Long,
    val currentLevel: LearningLevel = LearningLevel.REMEMBER,
    val streakDays: Int = 0,
    val longestStreak: Int = 0,
    val totalActivities: Int = 0,
    val totalTimeSeconds: Long = 0,
    val lastActivityAt: Long? = null,
    val levelStartedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
