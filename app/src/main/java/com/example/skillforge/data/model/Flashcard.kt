package com.example.skillforge.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = Skill::class,
            parentColumns = ["id"],
            childColumns = ["skillId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("skillId")]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val skillId: Long,
    val level: LearningLevel,
    val front: String,
    val back: String,
    val audioUri: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isUserCreated: Boolean = false
)

