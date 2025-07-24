package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

object GameSetupContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}