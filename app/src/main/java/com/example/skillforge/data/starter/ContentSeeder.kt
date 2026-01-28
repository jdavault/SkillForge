package com.example.skillforge.data.starter

import com.example.skillforge.data.model.Flashcard
import com.example.skillforge.data.model.Skill
import com.example.skillforge.data.model.UserProgress
import com.example.skillforge.data.repository.FlashcardRepository
import com.example.skillforge.data.repository.ProgressRepository
import com.example.skillforge.data.repository.SkillRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentSeeder @Inject constructor(
    private val skillRepository: SkillRepository,
    private val flashcardRepository: FlashcardRepository,
    private val progressRepository: ProgressRepository
) {
    suspend fun seedIfEmpty() {
        val existingSkills = skillRepository.getCount()
        if (existingSkills > 0) return

        // 1. Insert the skill and get its generated ID
        val skillId = skillRepository.insert(
            Skill(
                name = DrummerStarterContent.skillName,
                description = DrummerStarterContent.skillDescription,
                iconUri = DrummerStarterContent.skillIcon
            )
        )

        // 2. Map starter data to Flashcard entities with the real skillId
        val flashcards = DrummerStarterContent.flashcards.map { data ->
            Flashcard(
                skillId = skillId,
                level = data.level,
                front = data.front,
                back = data.back,
                imageUri = data.imageUri,
                audioUri = data.audioUri,
                isUserCreated = false
            )
        }
        flashcardRepository.insertAll(flashcards)

        // 3. Create initial progress record for this skill
        progressRepository.upsert(
            UserProgress(skillId = skillId)
        )
    }
}