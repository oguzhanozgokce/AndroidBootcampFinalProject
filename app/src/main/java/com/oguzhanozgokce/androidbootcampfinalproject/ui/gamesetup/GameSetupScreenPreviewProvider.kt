package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class GameSetupScreenPreviewProvider : PreviewParameterProvider<GameSetupContract.UiState> {
    override val values: Sequence<GameSetupContract.UiState>
        get() = sequenceOf(
            GameSetupContract.UiState(
                isLoading = true,
                list = emptyList(),
            ),
            GameSetupContract.UiState(
                isLoading = false,
                list = emptyList(),
            ),
            GameSetupContract.UiState(
                isLoading = false,
                list = listOf("Item 1", "Item 2", "Item 3")
            ),
        )
}