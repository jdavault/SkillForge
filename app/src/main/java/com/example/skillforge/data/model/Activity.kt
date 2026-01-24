package com.example.skillforge.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class ActivityType {
    FLASHCARD_STUDY,      // Studied a single card
    PRACTICE_SESSION,     // Completed a timed practice
    LEVEL_ASSESSMENT,     // Took a level-up challenge
    CONTENT_CREATED       // Created their own card/project
}

@Entity(
    tableName = "activities",
    foreignKeys = [
        ForeignKey(
            entity = Skill::class,
            parentColumns = ["id"],
            childColumns = ["skillId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("skillId"), Index("timestamp")]
)
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val skillId: Long,
    val type: ActivityType,
    val level: LearningLevel,
    val score: Float? = null,           // 0.0 to 1.0, null if not scored
    val durationSeconds: Int? = null,   // How long the activity took
    val flashcardId: Long? = null,      // Which card, if applicable
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: String? = null        // JSON for extra data
)
