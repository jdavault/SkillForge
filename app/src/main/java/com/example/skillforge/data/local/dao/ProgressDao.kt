package com.example.skillforge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.skillforge.data.model.LearningLevel
import com.example.skillforge.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM user_progress WHERE skillId = :skillId")
    fun getBySkillId(skillId: Long): Flow<UserProgress?>

    @Query("SELECT * FROM user_progress WHERE skillId = :skillId")
    suspend fun getBySkillIdOnce(skillId: Long): UserProgress?

    @Query("SELECT * FROM user_progress ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<UserProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: UserProgress): Long

    @Update
    suspend fun update(progress: UserProgress)

    @Query("UPDATE user_progress SET currentLevel = :level, levelStartedAt = :timestamp, updatedAt = :timestamp WHERE skillId = :skillId")
    suspend fun updateLevel(skillId: Long, level: LearningLevel, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_progress SET streakDays = :streak, longestStreak = MAX(longestStreak, :streak), updatedAt = :timestamp WHERE skillId = :skillId")
    suspend fun updateStreak(skillId: Long, streak: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_progress SET lastActivityAt = :timestamp, totalActivities = totalActivities + 1, updatedAt = :timestamp WHERE skillId = :skillId")
    suspend fun recordActivity(skillId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM user_progress WHERE skillId = :skillId")
    suspend fun deleteForSkill(skillId: Long)
}

