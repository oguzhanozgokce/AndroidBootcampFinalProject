package com.oguzhanozgokce.androidbootcampfinalproject.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.data.UserDataStore
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetCurrentUserUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetGameStatsUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getGameStatsUseCase: GetGameStatsUseCase,
    private val userDataStore: UserDataStore
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        viewModelScope.launch {
            loadUserInfo()
        }
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.LoadData -> loadUserInfo()
                is UiAction.LoadUserInfo -> loadUserInfo()
                is UiAction.NavigateToGameSetup -> emitUiEffect(UiEffect.NavigateToGameSetup)
                is UiAction.NavigateToScores -> emitUiEffect(UiEffect.NavigateToScores)
                is UiAction.NavigateToSettings -> emitUiEffect(UiEffect.NavigateToSettings)
                is UiAction.NavigateToScoreboard -> emitUiEffect(UiEffect.NavigateToGameScore)
                is UiAction.NavigateToTopScores -> emitUiEffect(UiEffect.NavigateToTopScores)
                is UiAction.NavigateToPage -> {
                    /* Handle navigation to specific page */
                }
            }
        }
    }

    private suspend fun loadUserInfo() {
        updateUiState { copy(isLoading = true) }
        userDataStore.getCurrentUser().collect { cachedUser ->
            if (cachedUser != null) {
                updateUiState { copy(user = cachedUser) }
                userDataStore.updateLastActiveTime()
                loadGameStats(cachedUser)
            } else {
                loadUserFromServer()
            }
        }
    }

    private fun loadUserFromServer() = viewModelScope.launch {
        val userResult = getCurrentUserUseCase()

        userResult.fold(
            onSuccess = { user ->
                if (user != null) {
                    userDataStore.saveUserData(user, rememberMe = true)

                    updateUiState { copy(user = user) }
                    loadGameStats(user)
                } else {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowError("Kullanıcı bilgileri bulunamadı"))
                }
            },
            onFailure = { userError ->
                updateUiState { copy(isLoading = false) }
                Log.e("HomeViewModel", "Error loading user info: ${userError.message}")
                emitUiEffect(UiEffect.ShowError("Kullanıcı bilgileri yüklenemedi: ${userError.message}"))
            }
        )
    }

    private fun loadGameStats(user: User) = viewModelScope.launch {
        getGameStatsUseCase().fold(
            onSuccess = { gameScores ->
                val gamesPlayed = gameScores.size
                val bestScore = gameScores.maxOfOrNull { it.score } ?: 0
                val completedGames = gameScores.count { it.completed }
                val totalScore = gameScores.sumOf { it.score }
                val winRate = if (gamesPlayed > 0) (completedGames * 100) / gamesPlayed else 0
                val averageScore = if (gamesPlayed > 0) totalScore / gamesPlayed else 0

                updateUiState {
                    copy(
                        isLoading = false,
                        user = user,
                        gamesPlayed = gamesPlayed,
                        bestScore = bestScore,
                        winRate = winRate,
                        totalScore = totalScore,
                        completedGames = completedGames,
                        averageScore = averageScore
                    )
                }
            },
            onFailure = { statsError ->
                updateUiState {
                    copy(
                        isLoading = false,
                        user = user,
                        gamesPlayed = 0,
                        bestScore = 0,
                        winRate = 0,
                        totalScore = 0,
                        completedGames = 0,
                        averageScore = 0
                    )
                }
                Log.e("HomeViewModel", "Error loading game stats: ${statsError.message}")
            }
        )
    }
}