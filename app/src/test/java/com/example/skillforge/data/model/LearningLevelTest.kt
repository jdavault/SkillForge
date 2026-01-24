package com.example.skillforge.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class LearningLevelTest {

    @Test
    fun `REMEMBER has order 1`() {
        assertEquals(1, LearningLevel.REMEMBER.order)
    }

    @Test
    fun `UNDERSTAND has order 2`() {
        assertEquals(2, LearningLevel.UNDERSTAND.order)
    }

    @Test
    fun `APPLY has order 3`() {
        assertEquals(3, LearningLevel.APPLY.order)
    }

    @Test
    fun `ANALYZE has order 4`() {
        assertEquals(4, LearningLevel.ANALYZE.order)
    }

    @Test
    fun `EVALUATE has order 5`() {
        assertEquals(5, LearningLevel.EVALUATE.order)
    }

    @Test
    fun `CREATE has order 6`() {
        assertEquals(6, LearningLevel.CREATE.order)
    }

    @Test
    fun `orders are sequential from 1 to 6`() {
        val orders = LearningLevel.entries.map { it.order }.sorted()
        assertEquals(listOf(1, 2, 3, 4, 5, 6), orders)
    }

    @Test
    fun `fromOrder returns correct level for each order`() {
        assertEquals(LearningLevel.REMEMBER, LearningLevel.fromOrder(1))
        assertEquals(LearningLevel.UNDERSTAND, LearningLevel.fromOrder(2))
        assertEquals(LearningLevel.APPLY, LearningLevel.fromOrder(3))
        assertEquals(LearningLevel.ANALYZE, LearningLevel.fromOrder(4))
        assertEquals(LearningLevel.EVALUATE, LearningLevel.fromOrder(5))
        assertEquals(LearningLevel.CREATE, LearningLevel.fromOrder(6))
    }

    @Test
    fun `all levels have non-empty displayName`() {
        LearningLevel.entries.forEach { level ->
            assert(level.displayName.isNotBlank()) { "$level has blank displayName" }
        }
    }

    @Test
    fun `all levels have non-empty description`() {
        LearningLevel.entries.forEach { level ->
            assert(level.description.isNotBlank()) { "$level has blank description" }
        }
    }

    @Test
    fun `levels can be compared by order`() {
        assert(LearningLevel.REMEMBER.order < LearningLevel.UNDERSTAND.order)
        assert(LearningLevel.UNDERSTAND.order < LearningLevel.APPLY.order)
        assert(LearningLevel.APPLY.order < LearningLevel.ANALYZE.order)
        assert(LearningLevel.ANALYZE.order < LearningLevel.EVALUATE.order)
        assert(LearningLevel.EVALUATE.order < LearningLevel.CREATE.order)
    }
}
