package com.oguzhanozgokce.androidbootcampfinalproject.ui.settings

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiState

class SettingsScreenPreviewProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState(
                isLoading = false,
                gameSettings = GameSettings(
                    userId = "test-user",
                    isDarkTheme = true,
                    isTimerEnabled = true,
                    gameTimeLimit = 60,
                    lastPlayerName = "Test Player"
                ),
                showClearScoresDialog = false,
                currentTheme = true
            ),
            UiState(
                isLoading = true,
                gameSettings = null,
                showClearScoresDialog = false,
                currentTheme = null
            )
        )
}