package com.example.skillforge.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skillforge.data.local.AppDatabase
import com.example.skillforge.data.model.LearningLevel
import com.example.skillforge.data.model.Skill
import com.example.skillforge.data.model.UserProgress
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProgressRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: ProgressRepository
    private lateinit var skillRepository: SkillRepository
    private var testSkillId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ProgressRepositoryImpl(database.progressDao())
        skillRepository = SkillRepositoryImpl(database.skillDao())

        testSkillId = skillRepository.insert(Skill(name = "Test Skill", description = ""))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun upsertAndRetrieveProgress() = runTest {
        val progress = UserProgress(
            skillId = testSkillId,
            currentLevel = LearningLevel.REMEMBER
        )

        repository.upsert(progress)
        val retrieved = repository.getBySkillIdOnce(testSkillId)

        assertNotNull(retrieved)
        assertEquals(LearningLevel.REMEMBER, retrieved?.currentLevel)
    }

    @Test
    fun upsertReplacesExisting() = runTest {
        val initial = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER)
        repository.upsert(initial)

        val updated = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.UNDERSTAND)
        repository.upsert(updated)

        val retrieved = repository.getBySkillIdOnce(testSkillId)
        assertEquals(LearningLevel.UNDERSTAND, retrieved?.currentLevel)
    }

    @Test
    fun updateLevel() = runTest {
        val progress = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER)
        repository.upsert(progress)

        repository.updateLevel(testSkillId, LearningLevel.APPLY)

        val retrieved = repository.getBySkillIdOnce(testSkillId)
        assertEquals(LearningLevel.APPLY, retrieved?.currentLevel)
    }

    @Test
    fun updateStreak() = runTest {
        val progress = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER, streakDays = 0)
        repository.upsert(progress)

        repository.updateStreak(testSkillId, streak = 5)

        val retrieved = repository.getBySkillIdOnce(testSkillId)
        assertEquals(5, retrieved?.streakDays)
        assertEquals(5, retrieved?.longestStreak) // Should update longest too
    }

    @Test
    fun recordActivityIncrementsCount() = runTest {
        val progress = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER, totalActivities = 0)
        repository.upsert(progress)

        repository.recordActivity(testSkillId)
        repository.recordActivity(testSkillId)

        val retrieved = repository.getBySkillIdOnce(testSkillId)
        assertEquals(2, retrieved?.totalActivities)
    }

    @Test
    fun getBySkillIdFlowEmitsUpdates() = runTest {
        val progress = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER)
        repository.upsert(progress)

        // Get initial value
        val flow = repository.getBySkillId(testSkillId)
        val initial = flow.first()
        assertEquals(LearningLevel.REMEMBER, initial?.currentLevel)
    }

    @Test
    fun getAllReturnsAllProgress() = runTest {
        // Create another skill
        val skill2Id = skillRepository.insert(Skill(name = "Skill 2", description = ""))

        repository.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER))
        repository.upsert(UserProgress(skillId = skill2Id, currentLevel = LearningLevel.UNDERSTAND))

        val all = repository.getAll().first()

        assertEquals(2, all.size)
    }

    @Test
    fun deleteForSkill() = runTest {
        repository.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER))

        repository.deleteForSkill(testSkillId)

        val retrieved = repository.getBySkillIdOnce(testSkillId)
        assertNull(retrieved)
    }
}
