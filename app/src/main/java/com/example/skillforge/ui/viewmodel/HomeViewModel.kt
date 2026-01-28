package com.example.skillforge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillforge.data.model.LearningLevel
import com.example.skillforge.data.model.Skill
import com.example.skillforge.data.model.UserProgress
import com.example.skillforge.data.repository.FlashcardRepository
import com.example.skillforge.data.repository.ProgressRepository
import com.example.skillforge.data.repository.SkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SkillWithProgress(
    val skill: Skill,
    val progress: UserProgress?,
    val flashcardCount: Int = 0
)

data class HomeUiState(
    val skills: List<SkillWithProgress> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    skillRepository: SkillRepository,
    progressRepository: ProgressRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        skillRepository.getAllActive(),
        progressRepository.getAll()
    ) { skills, progressList ->
        val skillsWithProgress = skills.map { skill ->
            SkillWithProgress(
                skill = skill,
                progress = progressList.find { it.skillId == skill.id }
            )
        }
        HomeUiState(
            skills = skillsWithProgress,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )
}
