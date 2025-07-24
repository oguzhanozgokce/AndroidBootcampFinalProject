package com.oguzhanozgokce.androidbootcampfinalproject.ui.score

object ScoreContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}