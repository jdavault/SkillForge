package com.example.skillforge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.skillforge.data.model.Activity
import com.example.skillforge.data.model.LearningLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities WHERE skillId = :skillId ORDER BY timestamp DESC")
    fun getBySkillId(skillId: Long): Flow<List<Activity>>

    @Query("SELECT * FROM activities WHERE skillId = :skillId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentBySkill(skillId: Long, limit: Int): List<Activity>

    @Query("SELECT * FROM activities WHERE skillId = :skillId AND timestamp > :since ORDER BY timestamp DESC")
    suspend fun getBySkillSince(skillId: Long, since: Long): List<Activity>

    @Query("SELECT * FROM activities WHERE skillId = :skillId AND level = :level ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentBySkillAndLevel(skillId: Long, level: LearningLevel, limit: Int): List<Activity>

    @Query("SELECT AVG(score) FROM activities WHERE skillId = :skillId AND score IS NOT NULL ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getAverageScoreRecent(skillId: Long, limit: Int): Float?

    @Query("SELECT COUNT(*) FROM activities WHERE skillId = :skillId AND timestamp > :since")
    suspend fun getCountSince(skillId: Long, since: Long): Int

    @Insert
    suspend fun insert(activity: Activity): Long

    @Query("DELETE FROM activities WHERE skillId = :skillId")
    suspend fun deleteAllForSkill(skillId: Long)
}
