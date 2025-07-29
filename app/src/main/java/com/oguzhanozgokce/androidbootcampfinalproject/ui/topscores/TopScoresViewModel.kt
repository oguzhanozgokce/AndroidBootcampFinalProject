package com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetTopScoresUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopScoresViewModel @Inject constructor(
    private val getTopScoresUseCase: GetTopScoresUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        onAction(UiAction.LoadTopScores)
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.LoadTopScores, UiAction.Refresh -> loadTopScores()
        }
    }

    private fun loadTopScores() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, error = null) }

            getTopScoresUseCase(20).fold(
                onSuccess = { scores ->
                    updateUiState {
                        copy(
                            isLoading = false,
                            topScores = scores,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    updateUiState {
                        copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                    Log.e("TopScoresViewModel", "Error loading top scores: ${error.message}")
                    emitUiEffect(UiEffect.ShowError(error.message ?: "Bir hata oluştu"))
                }
            )
        }
    }
}
