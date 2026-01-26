package com.example.skillforge.data.repository

import com.example.skillforge.data.local.dao.ProgressDao
import com.example.skillforge.data.model.LearningLevel
import com.example.skillforge.data.model.UserProgress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for UserProgress data access.
 */
interface ProgressRepository {
    fun getBySkillId(skillId: Long): Flow<UserProgress?>
    suspend fun getBySkillIdOnce(skillId: Long): UserProgress?
    fun getAll(): Flow<List<UserProgress>>
    suspend fun upsert(progress: UserProgress): Long
    suspend fun update(progress: UserProgress)
    suspend fun updateLevel(skillId: Long, level: LearningLevel, timestamp: Long = System.currentTimeMillis())
    suspend fun updateStreak(skillId: Long, streak: Int, timestamp: Long = System.currentTimeMillis())
    suspend fun recordActivity(skillId: Long, timestamp: Long = System.currentTimeMillis())
    suspend fun deleteForSkill(skillId: Long)
}

/**
 * Default implementation of ProgressRepository backed by Room.
 */
@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao
) : ProgressRepository {

    override fun getBySkillId(skillId: Long): Flow<UserProgress?> =
        progressDao.getBySkillId(skillId)

    override suspend fun getBySkillIdOnce(skillId: Long): UserProgress? =
        progressDao.getBySkillIdOnce(skillId)

    override fun getAll(): Flow<List<UserProgress>> =
        progressDao.getAll()

    override suspend fun upsert(progress: UserProgress): Long =
        progressDao.upsert(progress)

    override suspend fun update(progress: UserProgress) =
        progressDao.update(progress)

    override suspend fun updateLevel(skillId: Long, level: LearningLevel, timestamp: Long) =
        progressDao.updateLevel(skillId, level, timestamp)

    override suspend fun updateStreak(skillId: Long, streak: Int, timestamp: Long) =
        progressDao.updateStreak(skillId, streak, timestamp)

    override suspend fun recordActivity(skillId: Long, timestamp: Long) =
        progressDao.recordActivity(skillId, timestamp)

    override suspend fun deleteForSkill(skillId: Long) =
        progressDao.deleteForSkill(skillId)
}