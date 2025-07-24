package com.oguzhanozgokce.androidbootcampfinalproject.ui.game

object GameContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}