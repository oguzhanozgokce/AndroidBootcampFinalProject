package com.oguzhanozgokce.androidbootcampfinalproject.ui.home

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}