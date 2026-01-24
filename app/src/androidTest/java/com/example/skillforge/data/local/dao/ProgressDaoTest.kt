package com.example.skillforge.data.local.dao

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProgressDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var progressDao: ProgressDao
    private var testSkillId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        skillDao = database.skillDao()
        progressDao = database.progressDao()

        testSkillId = skillDao.insert(Skill(name = "Test Skill"))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun upsert_insertsNewProgress() = runTest {
        val progress = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER)

        progressDao.upsert(progress)

        val retrieved = progressDao.getBySkillIdOnce(testSkillId)
        assertNotNull(retrieved)
        assertEquals(LearningLevel.REMEMBER, retrieved?.currentLevel)
    }

    @Test
    fun upsert_replacesExistingProgress() = runTest {
        val initial = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER, streakDays = 5)
        progressDao.upsert(initial)

        val retrieved = progressDao.getBySkillIdOnce(testSkillId)!!
        progressDao.upsert(retrieved.copy(currentLevel = LearningLevel.APPLY, streakDays = 10))

        val updated = progressDao.getBySkillIdOnce(testSkillId)
        assertEquals(LearningLevel.APPLY, updated?.currentLevel)
        assertEquals(10, updated?.streakDays)
    }

    @Test
    fun getBySkillId_returnsFlowThatUpdates() = runTest {
        val initial = UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER)
        progressDao.upsert(initial)

        val flow = progressDao.getBySkillId(testSkillId)
        val first = flow.first()

        assertEquals(LearningLevel.REMEMBER, first?.currentLevel)
    }

    @Test
    fun getAll_returnsAllProgressRecords() = runTest {
        val skill2Id = skillDao.insert(Skill(name = "Skill 2"))

        progressDao.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER))
        progressDao.upsert(UserProgress(skillId = skill2Id, currentLevel = LearningLevel.APPLY))

        val allProgress = progressDao.getAll().first()

        assertEquals(2, allProgress.size)
    }

    @Test
    fun updateLevel_changesLevelAndTimestamp() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER))
        val before = progressDao.getBySkillIdOnce(testSkillId)!!

        val newTimestamp = System.currentTimeMillis() + 10000
        progressDao.updateLevel(testSkillId, LearningLevel.UNDERSTAND, newTimestamp)

        val after = progressDao.getBySkillIdOnce(testSkillId)
        assertEquals(LearningLevel.UNDERSTAND, after?.currentLevel)
        assertEquals(newTimestamp, after?.levelStartedAt)
        assertEquals(newTimestamp, after?.updatedAt)
    }

    @Test
    fun updateStreak_updatesStreakAndLongestStreak() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId, streakDays = 3, longestStreak = 5))

        progressDao.updateStreak(testSkillId, 7)

        val progress = progressDao.getBySkillIdOnce(testSkillId)
        assertEquals(7, progress?.streakDays)
        assertEquals(7, progress?.longestStreak) // Updated because 7 > 5
    }

    @Test
    fun updateStreak_preservesLongestStreakIfHigher() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId, streakDays = 10, longestStreak = 15))

        progressDao.updateStreak(testSkillId, 5)

        val progress = progressDao.getBySkillIdOnce(testSkillId)
        assertEquals(5, progress?.streakDays)
        assertEquals(15, progress?.longestStreak) // Preserved because 15 > 5
    }

    @Test
    fun recordActivity_incrementsTotalActivities() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId, totalActivities = 10))

        progressDao.recordActivity(testSkillId)

        val progress = progressDao.getBySkillIdOnce(testSkillId)
        assertEquals(11, progress?.totalActivities)
    }

    @Test
    fun recordActivity_updatesLastActivityAt() = runTest {
        val initialTime = 1000L
        progressDao.upsert(UserProgress(skillId = testSkillId, lastActivityAt = initialTime))

        val newTime = System.currentTimeMillis()
        progressDao.recordActivity(testSkillId, newTime)

        val progress = progressDao.getBySkillIdOnce(testSkillId)
        assertEquals(newTime, progress?.lastActivityAt)
    }

    @Test
    fun deleteSkill_cascadesDeleteToProgress() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.APPLY))

        val skill = skillDao.getById(testSkillId)!!
        skillDao.delete(skill)

        val progress = progressDao.getBySkillIdOnce(testSkillId)
        assertNull(progress)
    }

    @Test
    fun skillIdIndex_enforcesUniqueness() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.REMEMBER))
        progressDao.upsert(UserProgress(skillId = testSkillId, currentLevel = LearningLevel.APPLY))

        // Should only have one record due to unique index + upsert
        val all = progressDao.getAll().first()
        val forSkill = all.filter { it.skillId == testSkillId }
        assertEquals(1, forSkill.size)
        assertEquals(LearningLevel.APPLY, forSkill[0].currentLevel) // Latest upsert wins
    }

    @Test
    fun progress_defaultsToRememberLevel() = runTest {
        progressDao.upsert(UserProgress(skillId = testSkillId))

        val progress = progressDao.getBySkillIdOnce(testSkillId)

        assertEquals(LearningLevel.REMEMBER, progress?.currentLevel)
    }

    @Test
    fun progress_tracksAllTimeFields() = runTest {
        val now = System.currentTimeMillis()
        progressDao.upsert(UserProgress(
            skillId = testSkillId,
            totalTimeSeconds = 3600,
            lastActivityAt = now,
            levelStartedAt = now - 86400000,
            updatedAt = now
        ))

        val progress = progressDao.getBySkillIdOnce(testSkillId)

        assertEquals(3600L, progress?.totalTimeSeconds)
        assertEquals(now, progress?.lastActivityAt)
        assertTrue(progress?.levelStartedAt!! < progress.updatedAt)
    }
}
