package com.example.skillforge.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skillforge.data.local.AppDatabase
import com.example.skillforge.data.model.Skill
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SkillDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var skillDao: SkillDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        skillDao = database.skillDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertSkill_and_getById_returnsSkill() = runTest {
        val skill = Skill(name = "Drumming", description = "Learn drums")
        val id = skillDao.insert(skill)

        val retrieved = skillDao.getById(id)

        assertNotNull(retrieved)
        assertEquals("Drumming", retrieved?.name)
        assertEquals("Learn drums", retrieved?.description)
    }

    @Test
    fun getAllActive_excludesArchivedSkills() = runTest {
        skillDao.insert(Skill(name = "Active Skill 1"))
        skillDao.insert(Skill(name = "Active Skill 2"))
        val archivedId = skillDao.insert(Skill(name = "Archived Skill"))
        skillDao.archive(archivedId)

        val activeSkills = skillDao.getAllActive().first()

        assertEquals(2, activeSkills.size)
        assertTrue(activeSkills.none { it.name == "Archived Skill" })
    }

    @Test
    fun getAll_includesArchivedSkills() = runTest {
        skillDao.insert(Skill(name = "Active Skill"))
        val archivedId = skillDao.insert(Skill(name = "Archived Skill"))
        skillDao.archive(archivedId)

        val allSkills = skillDao.getAll().first()

        assertEquals(2, allSkills.size)
    }

    @Test
    fun archive_setsIsArchivedToTrue() = runTest {
        val id = skillDao.insert(Skill(name = "To Archive"))

        skillDao.archive(id)

        val skill = skillDao.getById(id)
        assertTrue(skill?.isArchived == true)
    }

    @Test
    fun update_modifiesSkill() = runTest {
        val id = skillDao.insert(Skill(name = "Original Name"))
        val original = skillDao.getById(id)!!

        skillDao.update(original.copy(name = "Updated Name"))

        val updated = skillDao.getById(id)
        assertEquals("Updated Name", updated?.name)
    }

    @Test
    fun delete_removesSkill() = runTest {
        val skill = Skill(name = "To Delete")
        val id = skillDao.insert(skill)
        val inserted = skillDao.getById(id)!!

        skillDao.delete(inserted)

        val deleted = skillDao.getById(id)
        assertNull(deleted)
    }

    @Test
    fun getAllActive_returnsSortedByName() = runTest {
        skillDao.insert(Skill(name = "Zebra"))
        skillDao.insert(Skill(name = "Apple"))
        skillDao.insert(Skill(name = "Mango"))

        val skills = skillDao.getAllActive().first()

        assertEquals("Apple", skills[0].name)
        assertEquals("Mango", skills[1].name)
        assertEquals("Zebra", skills[2].name)
    }
}
