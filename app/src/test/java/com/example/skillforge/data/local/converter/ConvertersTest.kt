package com.example.skillforge.data.local.converter

import com.example.skillforge.data.model.ActivityType
import com.example.skillforge.data.model.LearningLevel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConvertersTest {

    private lateinit var converters: Converters

    @Before
    fun setup() {
        converters = Converters()
    }

    // LearningLevel conversion tests

    @Test
    fun `fromLearningLevel converts REMEMBER to string`() {
        val result = converters.fromLearningLevel(LearningLevel.REMEMBER)
        assertEquals("REMEMBER", result)
    }

    @Test
    fun `toLearningLevel converts string to REMEMBER`() {
        val result = converters.toLearningLevel("REMEMBER")
        assertEquals(LearningLevel.REMEMBER, result)
    }

    @Test
    fun `LearningLevel round-trip for all values`() {
        LearningLevel.entries.forEach { level ->
            val asString = converters.fromLearningLevel(level)
            val backToLevel = converters.toLearningLevel(asString)
            assertEquals("Round-trip failed for $level", level, backToLevel)
        }
    }

    // ActivityType conversion tests

    @Test
    fun `fromActivityType converts FLASHCARD_STUDY to string`() {
        val result = converters.fromActivityType(ActivityType.FLASHCARD_STUDY)
        assertEquals("FLASHCARD_STUDY", result)
    }

    @Test
    fun `toActivityType converts string to FLASHCARD_STUDY`() {
        val result = converters.toActivityType("FLASHCARD_STUDY")
        assertEquals(ActivityType.FLASHCARD_STUDY, result)
    }

    @Test
    fun `ActivityType round-trip for all values`() {
        ActivityType.entries.forEach { type ->
            val asString = converters.fromActivityType(type)
            val backToType = converters.toActivityType(asString)
            assertEquals("Round-trip failed for $type", type, backToType)
        }
    }
}
