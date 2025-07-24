package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

enum class DifficultyLevel {
    EASY, MEDIUM, HARD
}

data class DifficultyInfo(
    val level: DifficultyLevel,
    val name: String,
    val cardCount: Int,
    val description: String
)

object GameSetupContract {
    data class UiState(
        val isLoading: Boolean = false,
        val selectedDifficulty: DifficultyLevel = DifficultyLevel.EASY,
        val availableDifficulties: List<DifficultyInfo> = listOf(
            DifficultyInfo(
                level = DifficultyLevel.EASY,
                name = "Kolay",
                cardCount = 12,
                description = "Yeni başlayanlar için ideal"
            ),
            DifficultyInfo(
                level = DifficultyLevel.MEDIUM,
                name = "Orta",
                cardCount = 16,
                description = "Biraz daha zorlu"
            ),
            DifficultyInfo(
                level = DifficultyLevel.HARD,
                name = "Zor",
                cardCount = 20,
                description = "Uzmanlar için"
            )
        )
    )

    sealed interface UiAction {
        data class OnDifficultyChanged(val difficulty: DifficultyLevel) : UiAction
        data object OnStartGameClicked : UiAction
    }

    sealed interface UiEffect {
        data class NavigateToGame(val difficulty: DifficultyLevel) : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}