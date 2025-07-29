package com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore

object TopScoresContract {
    data class UiState(
        val isLoading: Boolean = false,
        val topScores: List<GameScore> = emptyList(),
        val error: String? = null
    )

    sealed class UiAction {
        object LoadTopScores : UiAction()
        object Refresh : UiAction()
    }

    sealed class UiEffect {
        data class ShowError(val message: String) : UiEffect()
    }
}