
package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

object RegisterContract {
    data class UiState(
        val isLoading: Boolean = false,
        val displayName: String = "",
        val email: String = "",
        val password: String = "",
        val isPasswordVisible: Boolean = false
    )

    sealed class UiAction {
        data class OnDisplayNameChanged(val displayName: String) : UiAction()
        data class OnEmailChanged(val email: String) : UiAction()
        data class OnPasswordChanged(val password: String) : UiAction()
        data object OnPasswordVisibilityToggled : UiAction()
        data object OnRegisterClicked : UiAction()
        data object OnLoginClicked : UiAction()
    }

    sealed class UiEffect {
        data object NavigateToLogin : UiEffect()
        data object NavigateToHome : UiEffect()
        data class ShowError(val message: String) : UiEffect()
        data class ShowSuccess(val message: String) : UiEffect()
    }
}