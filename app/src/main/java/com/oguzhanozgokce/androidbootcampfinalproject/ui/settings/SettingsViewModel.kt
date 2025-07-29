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
                            this@SettingsViewModel.updateUiState {
                                copy(
                                    gameSettings = settings,
                                    isLoading = false,
                                    currentTheme = if (currentTheme == null) settings.isDarkTheme else currentTheme
                                )
                            }
                        }.onFailure { error ->
                            this@SettingsViewModel.updateUiState { copy(isLoading = false) }
                            emitUiEffect(UiEffect.ShowError(error.message ?: "Ayarlar yüklenemedi"))
                        }
                    }
                } ?: run {
                    this@SettingsViewModel.updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowError("Kullanıcı oturum açmamış"))
                }
            }.onFailure { error ->
                this@SettingsViewModel.updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(error.message ?: "Kullanıcı bulunamadı"))
            }
        }
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.ToggleTheme -> handleToggleTheme(uiAction.isDarkTheme)
                is UiAction.ToggleTimer -> handleToggleTimer(uiAction.isTimerEnabled)
                UiAction.ShowClearScoresDialog -> this@SettingsViewModel.updateUiState { copy(showClearScoresDialog = true) }
                UiAction.HideClearScoresDialog -> this@SettingsViewModel.updateUiState { copy(showClearScoresDialog = false) }
                UiAction.ClearScores -> handleClearScores()
            }
        }
    }

    private suspend fun handleToggleTheme(isDarkTheme: Boolean) {
        // Önce UI'ı anında güncelle
        this@SettingsViewModel.updateUiState { copy(currentTheme = isDarkTheme) }

        updateThemeUseCase(currentUserId, isDarkTheme).onSuccess {
            emitUiEffect(UiEffect.ThemeUpdated)
        }.onFailure { error ->
            emitUiEffect(UiEffect.ShowError(error.message ?: "Tema güncellenemedi"))
        }
    }

    private suspend fun handleToggleTimer(isTimerEnabled: Boolean) {
        updateTimerUseCase(currentUserId, isTimerEnabled).onSuccess {
            emitUiEffect(UiEffect.TimerUpdated)
        }.onFailure { error ->
            emitUiEffect(UiEffect.ShowError(error.message ?: "Timer ayarı güncellenemedi"))
        }
    }

    private suspend fun handleClearScores() {
        clearScoresUseCase(currentUserId).onSuccess {
            this@SettingsViewModel.updateUiState { copy(showClearScoresDialog = false) }
            emitUiEffect(UiEffect.ScoresCleared)
        }.onFailure { error ->
            this@SettingsViewModel.updateUiState { copy(showClearScoresDialog = false) }
            emitUiEffect(UiEffect.ShowError(error.message ?: "Skorlar temizlenemedi"))
        }
    }
}