package com.example.skillforge.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skillforge.data.local.AppDatabase
import com.example.skillforge.data.model.Flashcard
import com.example.skillforge.data.model.LearningLevel
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
class FlashcardDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var flashcardDao: FlashcardDao
    private var testSkillId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        skillDao = database.skillDao()
        flashcardDao = database.flashcardDao()

        // Create a skill for flashcards to belong to
        testSkillId = skillDao.insert(Skill(name = "Test Skill"))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertFlashcard_and_getById_returnsFlashcard() = runTest {
        val flashcard = Flashcard(
            skillId = testSkillId,
            level = LearningLevel.REMEMBER,
            front = "What is a paradiddle?",
            back = "RLRR LRLL"
        )
        val id = flashcardDao.insert(flashcard)

        val retrieved = flashcardDao.getById(id)

        assertNotNull(retrieved)
        assertEquals("What is a paradiddle?", retrieved?.front)
        assertEquals(LearningLevel.REMEMBER, retrieved?.level)
    }

    @Test
    fun getBySkillId_returnsOnlyFlashcardsForSkill() = runTest {
        val otherSkillId = skillDao.insert(Skill(name = "Other Skill"))

        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "Q1", back = "A1"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.UNDERSTAND, front = "Q2", back = "A2"))
        flashcardDao.insert(Flashcard(skillId = otherSkillId, level = LearningLevel.REMEMBER, front = "Other", back = "Other"))

        val flashcards = flashcardDao.getBySkillId(testSkillId).first()

        assertEquals(2, flashcards.size)
        assertTrue(flashcards.all { it.skillId == testSkillId })
    }

    @Test
    fun getBySkillAndLevel_filtersCorrectly() = runTest {
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "R1", back = "A1"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "R2", back = "A2"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.APPLY, front = "Apply1", back = "A3"))

        val rememberCards = flashcardDao.getBySkillAndLevel(testSkillId, LearningLevel.REMEMBER).first()
        val applyCards = flashcardDao.getBySkillAndLevel(testSkillId, LearningLevel.APPLY).first()

        assertEquals(2, rememberCards.size)
        assertEquals(1, applyCards.size)
    }

    @Test
    fun getCountForSkill_returnsCorrectCount() = runTest {
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "Q1", back = "A1"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.UNDERSTAND, front = "Q2", back = "A2"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.APPLY, front = "Q3", back = "A3"))

        val count = flashcardDao.getCountForSkill(testSkillId)

        assertEquals(3, count)
    }

    @Test
    fun getCountForSkillAndLevel_returnsCorrectCount() = runTest {
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "R1", back = "A1"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "R2", back = "A2"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.APPLY, front = "Apply", back = "A3"))

        val rememberCount = flashcardDao.getCountForSkillAndLevel(testSkillId, LearningLevel.REMEMBER)
        val applyCount = flashcardDao.getCountForSkillAndLevel(testSkillId, LearningLevel.APPLY)

        assertEquals(2, rememberCount)
        assertEquals(1, applyCount)
    }

    @Test
    fun insertAll_insertsBatchOfFlashcards() = runTest {
        val flashcards = listOf(
            Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "Q1", back = "A1"),
            Flashcard(skillId = testSkillId, level = LearningLevel.UNDERSTAND, front = "Q2", back = "A2"),
            Flashcard(skillId = testSkillId, level = LearningLevel.APPLY, front = "Q3", back = "A3")
        )

        flashcardDao.insertAll(flashcards)

        val count = flashcardDao.getCountForSkill(testSkillId)
        assertEquals(3, count)
    }

    @Test
    fun deleteSkill_cascadesDeleteToFlashcards() = runTest {
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.REMEMBER, front = "Q1", back = "A1"))
        flashcardDao.insert(Flashcard(skillId = testSkillId, level = LearningLevel.UNDERSTAND, front = "Q2", back = "A2"))

        val skill = skillDao.getById(testSkillId)!!
        skillDao.delete(skill)

        val count = flashcardDao.getCountForSkill(testSkillId)
        assertEquals(0, count)
    }

    @Test
    fun flashcard_storesMediaUris() = runTest {
        val flashcard = Flashcard(
            skillId = testSkillId,
            level = LearningLevel.REMEMBER,
            front = "Listen to this",
            back = "Answer",
            imageUri = "content://images/notation.png",
            audioUri = "content://audio/paradiddle.mp3"
        )
        val id = flashcardDao.insert(flashcard)

        val retrieved = flashcardDao.getById(id)

        assertEquals("content://images/notation.png", retrieved?.imageUri)
        assertEquals("content://audio/paradiddle.mp3", retrieved?.audioUri)
    }
}
