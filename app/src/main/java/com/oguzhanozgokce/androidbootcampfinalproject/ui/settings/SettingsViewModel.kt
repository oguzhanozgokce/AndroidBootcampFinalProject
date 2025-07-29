package com.oguzhanozgokce.androidbootcampfinalproject.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.ClearScoresUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetCurrentUserUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetGameSettingsUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.UpdateThemeUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.UpdateTimerUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateTimerUseCase: UpdateTimerUseCase,
    private val clearScoresUseCase: ClearScoresUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private var currentUserId: String = ""

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                user?.let { currentUser ->
                    currentUserId = currentUser.uid
                    getGameSettingsUseCase.flow(currentUserId).collectLatest { result ->
                        result.onSuccess { settings ->
                            updateUiState {
                                copy(
                                    gameSettings = settings,
                                    isLoading = false,
                                    currentTheme = settings.isDarkTheme,
                                    currentTimer = settings.isTimerEnabled
                                )
                            }
                        }.onFailure { error ->
                            updateUiState { copy(isLoading = false) }
                            emitUiEffect(UiEffect.ShowError(error.message ?: "Ayarlar yüklenemedi"))
                        }
                    }
                } ?: run {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowError("Kullanıcı oturum açmamış"))
                }
            }.onFailure { error ->
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(error.message ?: "Kullanıcı bulunamadı"))
            }
        }
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.ToggleTheme -> handleToggleTheme(uiAction.isDarkTheme)
                is UiAction.ToggleTimer -> handleToggleTimer(uiAction.isTimerEnabled)
                UiAction.ShowClearScoresDialog -> updateUiState { copy(showClearScoresDialog = true) }
                UiAction.HideClearScoresDialog -> updateUiState { copy(showClearScoresDialog = false) }
                UiAction.ClearScores -> handleClearScores()
            }
        }
    }

    private suspend fun handleToggleTheme(isDarkTheme: Boolean) {
        updateUiState { copy(currentTheme = isDarkTheme) }

        updateThemeUseCase(currentUserId, isDarkTheme).onSuccess {
            emitUiEffect(UiEffect.ThemeUpdated)
        }.onFailure { error ->
            updateUiState { copy(currentTheme = !isDarkTheme) }
            emitUiEffect(UiEffect.ShowError(error.message ?: "Tema güncellenemedi"))
        }
    }

    private suspend fun handleToggleTimer(isTimerEnabled: Boolean) {
        updateUiState { copy(currentTimer = isTimerEnabled) }
        updateTimerUseCase(currentUserId, isTimerEnabled).onSuccess {
            emitUiEffect(UiEffect.TimerUpdated)
        }.onFailure { error ->
            updateUiState { copy(currentTimer = !isTimerEnabled) }
            emitUiEffect(UiEffect.ShowError(error.message ?: "Timer ayarı güncellenemedi"))
        }
    }

    private suspend fun handleClearScores() {
        clearScoresUseCase(currentUserId).onSuccess {
            updateUiState { copy(showClearScoresDialog = false) }
            emitUiEffect(UiEffect.ScoresCleared)
        }.onFailure { error ->
            updateUiState { copy(showClearScoresDialog = false) }
            emitUiEffect(UiEffect.ShowError(error.message ?: "Skorlar temizlenemedi"))
        }
    }
}