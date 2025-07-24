package com.oguzhanozgokce.androidbootcampfinalproject.ui.home

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val user: User? = null,
        val gamesPlayed: Int = 0,
        val bestScore: Int = 0,
        val winRate: Int = 0,
        val totalScore: Int = 0,
        val completedGames: Int = 0,
        val averageScore: Int = 0,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction {
        data object LoadData : UiAction
        data object LoadUserInfo : UiAction
        data object NavigateToGameSetup : UiAction
        data object NavigateToScores : UiAction
        data object NavigateToSettings : UiAction
        data class NavigateToPage(val page: Int) : UiAction
        data object NavigateToScoreboard : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
        data object NavigateToGameSetup : UiEffect
        data object NavigateToScores : UiEffect
        data object NavigateToSettings : UiEffect
        data object NavigateToScoreboard : UiEffect
    }
}