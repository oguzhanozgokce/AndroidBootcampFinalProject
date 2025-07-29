package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetCurrentUserUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        loadUserName()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            getCurrentUserUseCase().onSuccess { user ->
                user?.let {
                    updateUiState {
                        copy(
                            playerName = it.displayName,
                            isLoading = false
                        )
                    }
                } ?: run {
                    updateUiState { copy(isLoading = false) }
                }
            }.onFailure { error ->
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(error.message ?: "Kullanıcı bilgileri yüklenemedi"))
            }
        }
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.OnPlayerNameChanged -> updateUiState { copy(playerName = uiAction.name) }
                is UiAction.OnDifficultyChanged -> updateUiState { copy(selectedDifficulty = uiAction.difficulty) }
                is UiAction.OnStartGameClicked -> startGame()
            }
        }
    }

    private fun startGame() = viewModelScope.launch {
        val currentState = currentUiState

        if (currentState.playerName.isBlank()) {
            emitUiEffect(UiEffect.ShowError("Oyuncu adı boş olamaz"))
            return@launch
        }

        emitUiEffect(UiEffect.NavigateToGame(currentState.selectedDifficulty, currentState.playerName.trim()))
    }
}