package com.oguzhanozgokce.androidbootcampfinalproject.ui.game

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class GameScreenPreviewProvider : PreviewParameterProvider<GameContract.UiState> {
    override val values: Sequence<GameContract.UiState>
        get() = sequenceOf(
            GameContract.UiState(
                isLoading = true,
                list = emptyList(),
            ),
            GameContract.UiState(
                isLoading = false,
                list = emptyList(),
            ),
            GameContract.UiState(
                isLoading = false,
                list = listOf("Item 1", "Item 2", "Item 3")
            ),
        )
}