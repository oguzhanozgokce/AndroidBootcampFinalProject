package com.oguzhanozgokce.androidbootcampfinalproject.ui.score

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ScoreScreenPreviewProvider : PreviewParameterProvider<ScoreContract.UiState> {
    override val values: Sequence<ScoreContract.UiState>
        get() = sequenceOf(
            ScoreContract.UiState(
                isLoading = true,
                list = emptyList(),
            ),
            ScoreContract.UiState(
                isLoading = false,
                list = emptyList(),
            ),
            ScoreContract.UiState(
                isLoading = false,
                list = listOf("Item 1", "Item 2", "Item 3")
            ),
        )
}