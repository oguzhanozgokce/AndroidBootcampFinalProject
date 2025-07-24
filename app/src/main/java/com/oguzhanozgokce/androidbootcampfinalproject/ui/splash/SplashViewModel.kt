package com.oguzhanozgokce.androidbootcampfinalproject.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.data.UserDataStore
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        checkAuthStatus()
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.CheckAuthStatus -> checkAuthStatus()
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            updateUiState {
                copy(
                    isLoading = true,
                    isCheckingAuth = true
                )
            }

            delay(1500)

            try {
                userDataStore.getUserSession().collect { session ->
                    if (session.isSessionValid && session.user != null) {
                        emitUiEffect(UiEffect.NavigateToHome)
                    } else {
                        if (session.user != null) {
                            userDataStore.clearAuthState()
                        }
                        emitUiEffect(UiEffect.NavigateToLogin)
                    }

                    updateUiState {
                        copy(
                            isLoading = false,
                            isCheckingAuth = false
                        )
                    }
                    return@collect
                }
            } catch (e: Exception) {
                emitUiEffect(UiEffect.NavigateToLogin)
                updateUiState {
                    copy(
                        isLoading = false,
                        isCheckingAuth = false
                    )
                }
            }
        }
    }
}