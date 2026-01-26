package com.example.skillforge.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skillforge.data.local.AppDatabase
import com.example.skillforge.data.model.Skill
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SkillRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: SkillRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = SkillRepositoryImpl(database.skillDao())
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveSkill() = runTest {
        val skill = Skill(name = "Drumming", description = "Learn drums")

        val id = repository.insert(skill)
        val retrieved = repository.getById(id)

        assertNotNull(retrieved)
        assertEquals("Drumming", retrieved?.name)
    }

    @Test
    fun getAllActiveExcludesArchived() = runTest {
        val active = Skill(name = "Active Skill", description = "")
        val archived = Skill(name = "Archived Skill", description = "", isArchived = true)

        repository.insert(active)
        repository.insert(archived)

        val activeSkills = repository.getAllActive().first()

        assertEquals(1, activeSkills.size)
        assertEquals("Active Skill", activeSkills[0].name)
    }

    @Test
    fun archiveSkill() = runTest {
        val skill = Skill(name = "To Archive", description = "")
        val id = repository.insert(skill)

        repository.archive(id)

        val retrieved = repository.getById(id)
        assertTrue(retrieved?.isArchived == true)
    }

    @Test
    fun updateSkill() = runTest {
        val skill = Skill(name = "Original", description = "")
        val id = repository.insert(skill)

        val updated = skill.copy(id = id, name = "Updated")
        repository.update(updated)

        val retrieved = repository.getById(id)
        assertEquals("Updated", retrieved?.name)
    }

    @Test
    fun deleteSkill() = runTest {
        val skill = Skill(name = "To Delete", description = "")
        val id = repository.insert(skill)

        val inserted = repository.getById(id)
        assertNotNull(inserted)

        repository.delete(inserted!!)

        val afterDelete = repository.getById(id)
        assertNull(afterDelete)
    }
}
