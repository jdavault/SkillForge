package com.example.skillforge.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillforge.data.model.Flashcard
import com.example.skillforge.data.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudySessionState(
    val flashcards: List<Flashcard> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val correctCount: Int = 0,
    val incorrectCount: Int = 0,
    val isLoading: Boolean = true,
    val isSessionComplete: Boolean = false
) {
    val currentCard: Flashcard? get() = flashcards.getOrNull(currentIndex)
    val totalCards: Int get() = flashcards.size
    val progress: Float get() = if (totalCards > 0) currentIndex.toFloat() / totalCards else 0f
}

@HiltViewModel
class FlashcardStudyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    private val skillId: Long = checkNotNull(savedStateHandle["skillId"])

    private val _uiState = MutableStateFlow(StudySessionState())
    val uiState: StateFlow<StudySessionState> = _uiState.asStateFlow()

    init {
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            val cards = flashcardRepository.getBySkillId(skillId).first().shuffled()
            _uiState.value = _uiState.value.copy(
                flashcards = cards,
                isLoading = false
            )
        }
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }

    fun markCorrect() {
        recordAnswerAndAdvance(correct = true)
    }

    fun markIncorrect() {
        recordAnswerAndAdvance(correct = false)
    }

    private fun recordAnswerAndAdvance(correct: Boolean) {
        val current = _uiState.value
        val newCorrect = if (correct) current.correctCount + 1 else current.correctCount
        val newIncorrect = if (!correct) current.incorrectCount + 1 else current.incorrectCount
        val nextIndex = current.currentIndex + 1
        val isComplete = nextIndex >= current.totalCards

        _uiState.value = current.copy(
            correctCount = newCorrect,
            incorrectCount = newIncorrect,
            currentIndex = nextIndex,
            isFlipped = false,
            isSessionComplete = isComplete
        )
    }

    fun restartSession() {
        _uiState.value = StudySessionState(isLoading = true)
        loadFlashcards()
    }
}
