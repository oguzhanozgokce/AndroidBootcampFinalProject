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
                is UiAction.NavigateToScoreboard -> emitUiEffect(UiEffect.NavigateToScoreboard)
                is UiAction.NavigateToPage -> {
                    /* Handle navigation to specific page */
                }
            }
        }
    }

    private suspend fun loadUserInfo() {
        updateUiState { copy(isLoading = true) }

        // First try to get user from DataStore (faster)
        userDataStore.getCurrentUser().collect { cachedUser ->
            if (cachedUser != null) {
                // Update UI with cached user immediately
                updateUiState { copy(user = cachedUser) }

                // Update last active time in DataStore
                userDataStore.updateLastActiveTime()

                // Then load fresh stats
                loadGameStats(cachedUser)
            } else {
                // If no cached user, fetch from server
                loadUserFromServer()
            }
        }
    }

    private suspend fun loadUserFromServer() {
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

    private suspend fun loadGameStats(user: User) {
        val statsResult = getGameStatsUseCase()

        Log.d("HomeViewModel", "Loading game stats for user: ${user.displayName}")

        statsResult.fold(
            onSuccess = { gameStats ->
                val averageScore = if (gameStats.gamesPlayed > 0) {
                    gameStats.totalScore / gameStats.gamesPlayed
                } else {
                    0
                }

                updateUiState {
                    copy(
                        isLoading = false,
                        user = user,
                        gamesPlayed = gameStats.gamesPlayed,
                        bestScore = gameStats.bestScore,
                        winRate = gameStats.winRate,
                        totalScore = gameStats.totalScore,
                        completedGames = gameStats.completedGames,
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
                        winRate = 0
                    )
                }
                Log.e("HomeViewModel", "Error loading game stats: ${statsError.message}")
                emitUiEffect(UiEffect.ShowError("İstatistikler yüklenemedi: ${statsError.message}"))
            }
        )
    }
}