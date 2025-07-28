package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore

object GameScoreContract {
    data class UiState(
        val isLoading: Boolean = false,
        val gameScores: List<GameScore> = emptyList(),
        val error: String? = null
    )

    sealed class UiAction {
        object LoadGameScores : UiAction()
        object RefreshScores : UiAction()
    }

    sealed class UiEffect {
        data class ShowError(val message: String) : UiEffect()
    }
}