package com.example.skillforge.data.repository

import com.example.skillforge.data.local.dao.SkillDao
import com.example.skillforge.data.model.Skill
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for Skill data access.
 * Abstracts the data source from the rest of the app.
 */
interface SkillRepository {
    fun getAllActive(): Flow<List<Skill>>
    fun getAll(): Flow<List<Skill>>
    suspend fun getById(skillId: Long): Skill?
    suspend fun insert(skill: Skill): Long
    suspend fun update(skill: Skill)
    suspend fun delete(skill: Skill)
    suspend fun archive(skillId: Long)
    suspend fun getCount(): Int
}

/**
 * Default implementation of SkillRepository backed by Room.
 */
@Singleton
class SkillRepositoryImpl @Inject constructor(
    private val skillDao: SkillDao
) : SkillRepository {

    override fun getAllActive(): Flow<List<Skill>> = skillDao.getAllActive()

    override fun getAll(): Flow<List<Skill>> = skillDao.getAll()

    override suspend fun getById(skillId: Long): Skill? = skillDao.getById(skillId)

    override suspend fun insert(skill: Skill): Long = skillDao.insert(skill)

    override suspend fun update(skill: Skill) = skillDao.update(skill)

    override suspend fun delete(skill: Skill) = skillDao.delete(skill)

    override suspend fun archive(skillId: Long) = skillDao.archive(skillId)

    override suspend fun getCount(): Int = skillDao.getCount()
}