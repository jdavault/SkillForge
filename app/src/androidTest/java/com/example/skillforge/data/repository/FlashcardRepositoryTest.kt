package com.example.skillforge.data.repository

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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlashcardRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: FlashcardRepository
    private lateinit var skillRepository: SkillRepository
    private var testSkillId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = FlashcardRepositoryImpl(database.flashcardDao())
        skillRepository = SkillRepositoryImpl(database.skillDao())

        // Create a skill to attach flashcards to
        testSkillId = skillRepository.insert(Skill(name = "Test Skill", description = ""))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveFlashcard() = runTest {
        val flashcard = Flashcard(
            skillId = testSkillId,
            front = "What is a paradiddle?",
            back = "RLRR LRLL pattern",
            level = LearningLevel.REMEMBER
        )

        val id = repository.insert(flashcard)
        val retrieved = repository.getById(id)

        assertNotNull(retrieved)
        assertEquals("What is a paradiddle?", retrieved?.front)
    }

    @Test
    fun getBySkillId() = runTest {
        val flashcard1 = Flashcard(skillId = testSkillId, front = "Q1", back = "A1", level = LearningLevel.REMEMBER)
        val flashcard2 = Flashcard(skillId = testSkillId, front = "Q2", back = "A2", level = LearningLevel.UNDERSTAND)

        repository.insert(flashcard1)
        repository.insert(flashcard2)

        val flashcards = repository.getBySkillId(testSkillId).first()

        assertEquals(2, flashcards.size)
    }

    @Test
    fun getBySkillAndLevel() = runTest {
        val remember = Flashcard(skillId = testSkillId, front = "R1", back = "A1", level = LearningLevel.REMEMBER)
        val understand = Flashcard(skillId = testSkillId, front = "U1", back = "A2", level = LearningLevel.UNDERSTAND)

        repository.insert(remember)
        repository.insert(understand)

        val rememberCards = repository.getBySkillAndLevel(testSkillId, LearningLevel.REMEMBER).first()

        assertEquals(1, rememberCards.size)
        assertEquals("R1", rememberCards[0].front)
    }

    @Test
    fun getCountForSkill() = runTest {
        repository.insert(Flashcard(skillId = testSkillId, front = "Q1", back = "A1", level = LearningLevel.REMEMBER))
        repository.insert(Flashcard(skillId = testSkillId, front = "Q2", back = "A2", level = LearningLevel.REMEMBER))

        val count = repository.getCountForSkill(testSkillId)

        assertEquals(2, count)
    }

    @Test
    fun insertAllFlashcards() = runTest {
        val flashcards = listOf(
            Flashcard(skillId = testSkillId, front = "Q1", back = "A1", level = LearningLevel.REMEMBER),
            Flashcard(skillId = testSkillId, front = "Q2", back = "A2", level = LearningLevel.REMEMBER),
            Flashcard(skillId = testSkillId, front = "Q3", back = "A3", level = LearningLevel.UNDERSTAND)
        )

        repository.insertAll(flashcards)

        val count = repository.getCountForSkill(testSkillId)
        assertEquals(3, count)
    }

    @Test
    fun deleteAllForSkill() = runTest {
        repository.insert(Flashcard(skillId = testSkillId, front = "Q1", back = "A1", level = LearningLevel.REMEMBER))
        repository.insert(Flashcard(skillId = testSkillId, front = "Q2", back = "A2", level = LearningLevel.REMEMBER))

        repository.deleteAllForSkill(testSkillId)

        val count = repository.getCountForSkill(testSkillId)
        assertEquals(0, count)
    }
}