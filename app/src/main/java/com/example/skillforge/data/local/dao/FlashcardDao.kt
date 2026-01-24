package com.example.skillforge.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.skillforge.data.model.Flashcard
import com.example.skillforge.data.model.LearningLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Query("SELECT * FROM flashcards WHERE skillId = :skillId ORDER BY createdAt DESC")
    fun getBySkillId(skillId: Long): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE skillId = :skillId AND level = :level ORDER BY createdAt DESC")
    fun getBySkillAndLevel(skillId: Long, level: LearningLevel): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE id = :flashcardId")
    suspend fun getById(flashcardId: Long): Flashcard?

    @Query("SELECT COUNT(*) FROM flashcards WHERE skillId = :skillId")
    suspend fun getCountForSkill(skillId: Long): Int

    @Query("SELECT COUNT(*) FROM flashcards WHERE skillId = :skillId AND level = :level")
    suspend fun getCountForSkillAndLevel(skillId: Long, level: LearningLevel): Int

    @Insert
    suspend fun insert(flashcard: Flashcard): Long

    @Insert
    suspend fun insertAll(flashcards: List<Flashcard>)

    @Update
    suspend fun update(flashcard: Flashcard)

    @Delete
    suspend fun delete(flashcard: Flashcard)

    @Query("DELETE FROM flashcards WHERE skillId = :skillId")
    suspend fun deleteAllForSkill(skillId: Long)
}
