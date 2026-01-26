package com.example.skillforge.data.repository

import com.example.skillforge.data.local.dao.ActivityDao
import com.example.skillforge.data.model.Activity
import com.example.skillforge.data.model.LearningLevel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for Activity (learning session) data access.
 */
interface ActivityRepository {
    fun getBySkillId(skillId: Long): Flow<List<Activity>>
    suspend fun getRecentBySkill(skillId: Long, limit: Int): List<Activity>
    suspend fun getBySkillSince(skillId: Long, since: Long): List<Activity>
    suspend fun getRecentBySkillAndLevel(skillId: Long, level: LearningLevel, limit: Int): List<Activity>
    suspend fun getAverageScoreRecent(skillId: Long, limit: Int): Float?
    suspend fun getCountSince(skillId: Long, since: Long): Int
    suspend fun insert(activity: Activity): Long
    suspend fun deleteAllForSkill(skillId: Long)
}

/**
 * Default implementation of ActivityRepository backed by Room.
 */
@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository {

    override fun getBySkillId(skillId: Long): Flow<List<Activity>> =
        activityDao.getBySkillId(skillId)

    override suspend fun getRecentBySkill(skillId: Long, limit: Int): List<Activity> =
        activityDao.getRecentBySkill(skillId, limit)

    override suspend fun getBySkillSince(skillId: Long, since: Long): List<Activity> =
        activityDao.getBySkillSince(skillId, since)

    override suspend fun getRecentBySkillAndLevel(
        skillId: Long,
        level: LearningLevel,
        limit: Int
    ): List<Activity> = activityDao.getRecentBySkillAndLevel(skillId, level, limit)

    override suspend fun getAverageScoreRecent(skillId: Long, limit: Int): Float? =
        activityDao.getAverageScoreRecent(skillId, limit)

    override suspend fun getCountSince(skillId: Long, since: Long): Int =
        activityDao.getCountSince(skillId, since)

    override suspend fun insert(activity: Activity): Long =
        activityDao.insert(activity)

    override suspend fun deleteAllForSkill(skillId: Long) =
        activityDao.deleteAllForSkill(skillId)
}