package com.oguzhanozgokce.androidbootcampfinalproject.ui.game

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameCard
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameResult

object GameContract {
    data class UiState(
        val isLoading: Boolean = false,
        val difficulty: GameDifficulty = GameDifficulty.EASY,
        val cards: List<GameCard> = emptyList(),
        val score: Int = 0,
        val timeRemaining: Int = 60,
        val flippedCards: List<GameCard> = emptyList(),
        val matchedPairs: Int = 0,
        val totalMoves: Int = 0,
        val isGameCompleted: Boolean = false,
        val isGameOver: Boolean = false,
        val gameResult: GameResult = GameResult.InProgress,
        val canFlipCards: Boolean = true,
        val showGameCompleteDialog: Boolean = false,
        val showGameOverDialog: Boolean = false
    )

    sealed interface UiAction {
        data object LoadGame : UiAction
        data class OnCardClicked(val cardId: String) : UiAction
        data object OnGameCompleteDialogDismiss : UiAction
        data object OnGameOverDialogDismiss : UiAction
        data object OnRestartGame : UiAction
        data object OnSaveScore : UiAction
        data object OnBackClicked : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data object NavigateBack : UiEffect
        data object ShowGameCompleteDialog : UiEffect
        data object ShowGameOverDialog : UiEffect
        data class ShowScoreSaved(val score: Int) : UiEffect
    }
}