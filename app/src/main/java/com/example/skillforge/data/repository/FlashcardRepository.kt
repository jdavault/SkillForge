package com.example.skillforge.data.repository

import com.example.skillforge.data.local.dao.FlashcardDao
import com.example.skillforge.data.model.Flashcard
import com.example.skillforge.data.model.LearningLevel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for Flashcard data access.
 */
interface FlashcardRepository {
    fun getBySkillId(skillId: Long): Flow<List<Flashcard>>
    fun getBySkillAndLevel(skillId: Long, level: LearningLevel): Flow<List<Flashcard>>
    suspend fun getById(flashcardId: Long): Flashcard?
    suspend fun getCountForSkill(skillId: Long): Int
    suspend fun getCountForSkillAndLevel(skillId: Long, level: LearningLevel): Int
    suspend fun insert(flashcard: Flashcard): Long
    suspend fun insertAll(flashcards: List<Flashcard>)
    suspend fun update(flashcard: Flashcard)
    suspend fun delete(flashcard: Flashcard)
    suspend fun deleteAllForSkill(skillId: Long)
}

/**
 * Default implementation of FlashcardRepository backed by Room.
 */
@Singleton
class FlashcardRepositoryImpl @Inject constructor(
    private val flashcardDao: FlashcardDao
) : FlashcardRepository {

    override fun getBySkillId(skillId: Long): Flow<List<Flashcard>> =
        flashcardDao.getBySkillId(skillId)

    override fun getBySkillAndLevel(skillId: Long, level: LearningLevel): Flow<List<Flashcard>> =
        flashcardDao.getBySkillAndLevel(skillId, level)

    override suspend fun getById(flashcardId: Long): Flashcard? =
        flashcardDao.getById(flashcardId)

    override suspend fun getCountForSkill(skillId: Long): Int =
        flashcardDao.getCountForSkill(skillId)

    override suspend fun getCountForSkillAndLevel(skillId: Long, level: LearningLevel): Int =
        flashcardDao.getCountForSkillAndLevel(skillId, level)

    override suspend fun insert(flashcard: Flashcard): Long =
        flashcardDao.insert(flashcard)

    override suspend fun insertAll(flashcards: List<Flashcard>) =
        flashcardDao.insertAll(flashcards)

    override suspend fun update(flashcard: Flashcard) =
        flashcardDao.update(flashcard)

    override suspend fun delete(flashcard: Flashcard) =
        flashcardDao.delete(flashcard)

    override suspend fun deleteAllForSkill(skillId: Long) =
        flashcardDao.deleteAllForSkill(skillId)
}