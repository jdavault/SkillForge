package com.example.skillforge.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skillforge.data.local.AppDatabase
import com.example.skillforge.data.model.Activity
import com.example.skillforge.data.model.ActivityType
import com.example.skillforge.data.model.LearningLevel
import com.example.skillforge.data.model.Skill
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var activityDao: ActivityDao
    private var testSkillId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        skillDao = database.skillDao()
        activityDao = database.activityDao()

        testSkillId = skillDao.insert(Skill(name = "Test Skill"))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertActivity_and_getBySkillId_returnsActivity() = runTest {
        val activity = Activity(
            skillId = testSkillId,
            type = ActivityType.FLASHCARD_STUDY,
            level = LearningLevel.REMEMBER,
            score = 0.8f
        )
        activityDao.insert(activity)

        val activities = activityDao.getBySkillId(testSkillId).first()

        assertEquals(1, activities.size)
        assertEquals(ActivityType.FLASHCARD_STUDY, activities[0].type)
        assertEquals(0.8f, activities[0].score)
    }

    @Test
    fun getRecentBySkill_respectsLimit() = runTest {
        repeat(10) { i ->
            activityDao.insert(
                Activity(
                    skillId = testSkillId,
                    type = ActivityType.FLASHCARD_STUDY,
                    level = LearningLevel.REMEMBER,
                    score = i * 0.1f,
                    timestamp = System.currentTimeMillis() + i
                )
            )
        }

        val recent5 = activityDao.getRecentBySkill(testSkillId, 5)

        assertEquals(5, recent5.size)
    }

    @Test
    fun getRecentBySkill_returnsMostRecentFirst() = runTest {
        val baseTime = System.currentTimeMillis()
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.1f, timestamp = baseTime))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.2f, timestamp = baseTime + 1000))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.3f, timestamp = baseTime + 2000))

        val recent = activityDao.getRecentBySkill(testSkillId, 10)

        assertEquals(0.3f, recent[0].score) // Most recent
        assertEquals(0.2f, recent[1].score)
        assertEquals(0.1f, recent[2].score) // Oldest
    }

    @Test
    fun getBySkillSince_filtersOldActivities() = runTest {
        val now = System.currentTimeMillis()
        val oneWeekAgo = now - (7 * 24 * 60 * 60 * 1000L)
        val twoWeeksAgo = now - (14 * 24 * 60 * 60 * 1000L)

        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = now))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = oneWeekAgo + 1000))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = twoWeeksAgo))

        val activitiesSinceLastWeek = activityDao.getBySkillSince(testSkillId, oneWeekAgo)

        assertEquals(2, activitiesSinceLastWeek.size)
    }

    @Test
    fun getRecentBySkillAndLevel_filtersCorrectly() = runTest {
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.5f))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.6f))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.APPLY, score = 0.7f))

        val rememberActivities = activityDao.getRecentBySkillAndLevel(testSkillId, LearningLevel.REMEMBER, 10)
        val applyActivities = activityDao.getRecentBySkillAndLevel(testSkillId, LearningLevel.APPLY, 10)

        assertEquals(2, rememberActivities.size)
        assertEquals(1, applyActivities.size)
    }

    @Test
    fun getAverageScoreRecent_calculatesCorrectly() = runTest {
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.6f, timestamp = 1))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.8f, timestamp = 2))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 1.0f, timestamp = 3))

        val average = activityDao.getAverageScoreRecent(testSkillId, 3)

        assertEquals(0.8f, average!!, 0.01f)
    }

    @Test
    fun getAverageScoreRecent_ignoresNullScores() = runTest {
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.5f))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.PRACTICE_SESSION, level = LearningLevel.REMEMBER, score = null))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 1.0f))

        val average = activityDao.getAverageScoreRecent(testSkillId, 10)

        assertEquals(0.75f, average!!, 0.01f)
    }

    @Test
    fun getAverageScoreRecent_returnsNullWhenNoScoredActivities() = runTest {
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.PRACTICE_SESSION, level = LearningLevel.REMEMBER, score = null))

        val average = activityDao.getAverageScoreRecent(testSkillId, 10)

        assertNull(average)
    }

    @Test
    fun getCountSince_countsCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val oneHourAgo = now - (60 * 60 * 1000L)

        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = now))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = now - 1000))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = oneHourAgo - 1000))

        val countSinceLastHour = activityDao.getCountSince(testSkillId, oneHourAgo)

        assertEquals(2, countSinceLastHour)
    }

    @Test
    fun deleteSkill_cascadesDeleteToActivities() = runTest {
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER))
        activityDao.insert(Activity(skillId = testSkillId, type = ActivityType.PRACTICE_SESSION, level = LearningLevel.UNDERSTAND))

        val skill = skillDao.getById(testSkillId)!!
        skillDao.delete(skill)

        val activities = activityDao.getBySkillId(testSkillId).first()
        assertTrue(activities.isEmpty())
    }
}
