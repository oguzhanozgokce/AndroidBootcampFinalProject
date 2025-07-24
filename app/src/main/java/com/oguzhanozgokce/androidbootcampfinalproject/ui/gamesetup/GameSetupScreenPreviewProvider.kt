package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty

class GameSetupScreenPreviewProvider : PreviewParameterProvider<GameSetupContract.UiState> {
    override val values: Sequence<GameSetupContract.UiState>
        get() = sequenceOf(
            GameSetupContract.UiState(
                isLoading = true,
                selectedDifficulty = GameDifficulty.EASY
            ),
            GameSetupContract.UiState(
                isLoading = false,
                selectedDifficulty = GameDifficulty.EASY
            ),
            GameSetupContract.UiState(
                isLoading = false,
                selectedDifficulty = GameDifficulty.MEDIUM
            ),
            GameSetupContract.UiState(
                isLoading = false,
                selectedDifficulty = GameDifficulty.HARD
            )
        )
}