package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty

data class DifficultyInfo(
    val level: GameDifficulty,
    val name: String,
    val description: String
)

object GameSetupContract {
    data class UiState(
        val isLoading: Boolean = false,
        val playerName: String = "",
        val selectedDifficulty: GameDifficulty = GameDifficulty.EASY,
        val availableDifficulties: List<DifficultyInfo> = listOf(
            DifficultyInfo(
                level = GameDifficulty.EASY,
                name = "Kolay",
                description = "Yeni başlayanlar için ideal"
            ),
            DifficultyInfo(
                level = GameDifficulty.MEDIUM,
                name = "Orta",
                description = "Biraz daha zorlu"
            ),
            DifficultyInfo(
                level = GameDifficulty.HARD,
                name = "Zor",
                description = "Uzmanlar için"
            )
        )
    )

    sealed interface UiAction {
        data class OnPlayerNameChanged(val name: String) : UiAction
        data class OnDifficultyChanged(val difficulty: GameDifficulty) : UiAction
        data object OnStartGameClicked : UiAction
    }

    sealed interface UiEffect {
        data class NavigateToGame(val difficulty: GameDifficulty, val playerName: String) : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}