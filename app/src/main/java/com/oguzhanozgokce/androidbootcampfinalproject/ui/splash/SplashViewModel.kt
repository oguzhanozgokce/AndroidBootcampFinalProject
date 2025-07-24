package com.oguzhanozgokce.androidbootcampfinalproject.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
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

            authRepository.getCurrentUser()
                .fold(
                    onSuccess = { user ->
                        if (user != null) {
                            emitUiEffect(UiEffect.NavigateToHome)
                        } else {
                            emitUiEffect(UiEffect.NavigateToLogin)
                        }
                    },
                    onFailure = {
                        emitUiEffect(UiEffect.NavigateToLogin)
                    }
                )

            updateUiState {
                copy(
                    isLoading = false,
                    isCheckingAuth = false
                )
            }
        }
    }
}