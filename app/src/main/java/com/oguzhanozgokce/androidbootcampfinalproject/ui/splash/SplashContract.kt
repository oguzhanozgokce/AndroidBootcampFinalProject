package com.oguzhanozgokce.androidbootcampfinalproject.ui.splash

object SplashContract {
    data class UiState(
        val isLoading: Boolean = true,
        val isCheckingAuth: Boolean = true
    )

    sealed interface UiAction {
        data object CheckAuthStatus : UiAction
    }

    sealed interface UiEffect {
        data object NavigateToLogin : UiEffect
        data object NavigateToHome : UiEffect
    }
}