package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login

object LoginContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val isFormValid: Boolean = false,
        val rememberMe: Boolean = false
    )

    sealed class UiAction {
        data class OnEmailChanged(val email: String) : UiAction()
        data class OnPasswordChanged(val password: String) : UiAction()
        data object OnPasswordVisibilityToggled : UiAction()
        data class OnRememberMeChanged(val rememberMe: Boolean) : UiAction()
        data object OnLoginClicked : UiAction()
        data object OnSignUpClicked : UiAction()
        data object OnForgotPasswordClicked : UiAction()
    }

    sealed class UiEffect {
        data object NavigateToHome : UiEffect()
        data object NavigateToSignUp : UiEffect()
        data object NavigateToForgotPassword : UiEffect()
        data class ShowError(val message: String) : UiEffect()
        data class ShowSuccess(val message: String) : UiEffect()
    }
}