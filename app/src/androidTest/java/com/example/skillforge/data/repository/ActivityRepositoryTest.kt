package com.example.skillforge.data.repository

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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: ActivityRepository
    private lateinit var skillRepository: SkillRepository
    private var testSkillId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ActivityRepositoryImpl(database.activityDao())
        skillRepository = SkillRepositoryImpl(database.skillDao())

        testSkillId = skillRepository.insert(Skill(name = "Test Skill", description = ""))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveActivity() = runTest {
        val activity = Activity(
            skillId = testSkillId,
            type = ActivityType.FLASHCARD_STUDY,
            level = LearningLevel.REMEMBER,
            score = 0.85f
        )

        val id = repository.insert(activity)
        val activities = repository.getBySkillId(testSkillId).first()

        assertEquals(1, activities.size)
        assertEquals(0.85f, activities[0].score)
    }

    @Test
    fun getRecentBySkill() = runTest {
        // Insert 5 activities
        repeat(5) { i ->
            repository.insert(Activity(
                skillId = testSkillId,
                type = ActivityType.FLASHCARD_STUDY,
                level = LearningLevel.REMEMBER,
                score = (i + 1) * 0.1f
            ))
        }

        val recent = repository.getRecentBySkill(testSkillId, limit = 3)

        assertEquals(3, recent.size)
    }

    @Test
    fun getAverageScoreRecent() = runTest {
        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.8f))
        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 0.9f))
        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, score = 1.0f))

        val avg = repository.getAverageScoreRecent(testSkillId, limit = 3)

        assertNotNull(avg)
        assertEquals(0.9f, avg!!, 0.01f)
    }

    @Test
    fun getBySkillSince() = runTest {
        val now = System.currentTimeMillis()
        val dayAgo = now - 24 * 60 * 60 * 1000

        // This activity is "old" - we manually set timestamp
        repository.insert(Activity(
            skillId = testSkillId,
            type = ActivityType.FLASHCARD_STUDY,
            level = LearningLevel.REMEMBER,
            timestamp = dayAgo - 1000 // Just before cutoff
        ))

        // This activity is "recent"
        repository.insert(Activity(
            skillId = testSkillId,
            type = ActivityType.FLASHCARD_STUDY,
            level = LearningLevel.REMEMBER,
            timestamp = now
        ))

        val recentActivities = repository.getBySkillSince(testSkillId, dayAgo)

        assertEquals(1, recentActivities.size)
    }

    @Test
    fun getCountSince() = runTest {
        val now = System.currentTimeMillis()
        val hourAgo = now - 60 * 60 * 1000

        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = now))
        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER, timestamp = now))

        val count = repository.getCountSince(testSkillId, hourAgo)

        assertEquals(2, count)
    }

    @Test
    fun deleteAllForSkill() = runTest {
        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER))
        repository.insert(Activity(skillId = testSkillId, type = ActivityType.FLASHCARD_STUDY, level = LearningLevel.REMEMBER))

        repository.deleteAllForSkill(testSkillId)

        val activities = repository.getBySkillId(testSkillId).first()
        assertTrue(activities.isEmpty())
    }
}
