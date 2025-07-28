package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetGameStatsUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScoreViewModel @Inject constructor(
    private val getGameStatsUseCase: GetGameStatsUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        loadGameScores()
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.LoadGameScores -> loadGameScores()
            is UiAction.RefreshScores -> refreshScores()
        }
    }

    private fun loadGameScores() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, error = null) }

            getGameStatsUseCase().fold(
                onSuccess = { scores ->
                    updateUiState {
                        copy(
                            isLoading = false,
                            gameScores = scores.sortedByDescending { it.timestamp },
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: "Unknown error occurred"
                    updateUiState {
                        copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    emitUiEffect(UiEffect.ShowError(errorMessage))
                }
            )
        }
    }

    private fun refreshScores() {
        loadGameScores()
    }
}
