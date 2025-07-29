package com.oguzhanozgokce.androidbootcampfinalproject.ui.settings

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings

object SettingsContract {
    data class UiState(
        val isLoading: Boolean = false,
        val gameSettings: GameSettings? = null,
        val showClearScoresDialog: Boolean = false,
        val currentTheme: Boolean? = null,
    )

    sealed interface UiAction {
        data class ToggleTheme(val isDarkTheme: Boolean) : UiAction
        data class ToggleTimer(val isTimerEnabled: Boolean) : UiAction
        data object ShowClearScoresDialog : UiAction
        data object HideClearScoresDialog : UiAction
        data object ClearScores : UiAction
    }

    sealed interface UiEffect {
        data object ScoresCleared : UiEffect
        data object ThemeUpdated : UiEffect
        data object TimerUpdated : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}