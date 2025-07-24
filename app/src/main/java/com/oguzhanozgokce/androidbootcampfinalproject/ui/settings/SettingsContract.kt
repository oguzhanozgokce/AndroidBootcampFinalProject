package com.oguzhanozgokce.androidbootcampfinalproject.ui.settings

object SettingsContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}