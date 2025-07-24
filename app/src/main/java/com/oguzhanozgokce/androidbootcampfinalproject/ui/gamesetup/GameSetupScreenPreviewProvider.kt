package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class GameSetupScreenPreviewProvider : PreviewParameterProvider<GameSetupContract.UiState> {
    override val values: Sequence<GameSetupContract.UiState>
        get() = sequenceOf(
            GameSetupContract.UiState(
                isLoading = true,
                selectedDifficulty = DifficultyLevel.EASY
            ),
            GameSetupContract.UiState(
                isLoading = false,
                selectedDifficulty = DifficultyLevel.EASY
            ),
            GameSetupContract.UiState(
                isLoading = false,
                selectedDifficulty = DifficultyLevel.MEDIUM
            ),
            GameSetupContract.UiState(
                isLoading = false,
                selectedDifficulty = DifficultyLevel.HARD
            )
        )
}