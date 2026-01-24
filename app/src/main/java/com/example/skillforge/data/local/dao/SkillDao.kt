package com.example.skillforge.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.skillforge.data.model.Skill
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao {

    @Query("SELECT * FROM skills WHERE isArchived = 0 ORDER BY name ASC")
    fun getAllActive(): Flow<List<Skill>>

    @Query("SELECT * FROM skills ORDER BY name ASC")
    fun getAll(): Flow<List<Skill>>

    @Query("SELECT * FROM skills WHERE id = :skillId")
    suspend fun getById(skillId: Long): Skill?

    @Insert
    suspend fun insert(skill: Skill): Long

    @Update
    suspend fun update(skill: Skill)

    @Delete
    suspend fun delete(skill: Skill)

    @Query("UPDATE skills SET isArchived = 1 WHERE id = :skillId")
    suspend fun archive(skillId: Long)
}
