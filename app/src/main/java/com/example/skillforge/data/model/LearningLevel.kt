package com.example.skillforge.data.model

enum class LearningLevel(
    val order: Int,
    val displayName: String,
    val description: String
) {
    REMEMBER(
        order = 1,
        displayName = "Remember",
        description = "Recall facts and basic concepts"
    ),
    UNDERSTAND(
        order = 2,
        displayName = "Understand",
        description = "Explain ideas and concepts"
    ),
    APPLY(
        order = 3,
        displayName = "Apply",
        description = "Use information in new situations"
    ),
    ANALYZE(
        order = 4,
        displayName = "Analyze",
        description = "Draw connections among ideas"
    ),
    EVALUATE(
        order = 5,
        displayName = "Evaluate",
        description = "Justify decisions and actions"
    ),
    CREATE(
        order = 6,
        displayName = "Create",
        description = "Produce new or original work"
    );

    companion object {
        fun fromOrder(order: Int): LearningLevel =
            entries.first { it.order == order }
    }
}
