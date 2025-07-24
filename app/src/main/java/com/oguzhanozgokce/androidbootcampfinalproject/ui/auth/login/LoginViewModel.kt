package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.SignInUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.common.exception.ErrorHandler
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnEmailChanged -> handleEmailChanged(uiAction.email)
            is UiAction.OnPasswordChanged -> handlePasswordChanged(uiAction.password)
            is UiAction.OnPasswordVisibilityToggled -> togglePasswordVisibility()
            is UiAction.OnLoginClicked -> handleLogin()
            is UiAction.OnSignUpClicked -> handleSignUp()
            is UiAction.OnForgotPasswordClicked -> handleForgotPassword()
        }
    }

    private fun handleEmailChanged(email: String) {
        updateUiState {
            copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
        validateForm()
    }

    private fun handlePasswordChanged(password: String) {
        updateUiState {
            copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
        validateForm()
    }

    private fun togglePasswordVisibility() {
        updateUiState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    private fun handleLogin() {
        val currentState = uiState.value
        if (!currentState.isFormValid) return

        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            signInUseCase(currentState.email, currentState.password)
                .fold(
                    onSuccess = { user ->
                        updateUiState { copy(isLoading = false) }
                        emitUiEffect(UiEffect.ShowSuccess("Giriş başarılı! Hoş geldiniz."))
                        emitUiEffect(UiEffect.NavigateToHome)
                    },
                    onFailure = { exception ->
                        updateUiState { copy(isLoading = false) }
                        val errorMessage = ErrorHandler.handleException(exception)
                        emitUiEffect(UiEffect.ShowError(errorMessage))
                    }
                )
        }
    }

    private fun handleSignUp() = viewModelScope.launch {
        emitUiEffect(UiEffect.NavigateToSignUp)
    }

    private fun handleForgotPassword() = viewModelScope.launch {
        emitUiEffect(UiEffect.NavigateToForgotPassword)
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "E-posta adresi gerekli"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Geçerli bir e-posta adresi girin"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Şifre gerekli"
            password.length < 6 -> "Şifre en az 6 karakter olmalı"
            else -> null
        }
    }

    private fun validateForm() {
        val currentState = uiState.value
        val isValid = currentState.emailError == null &&
                currentState.passwordError == null &&
                currentState.email.isNotBlank() &&
                currentState.password.isNotBlank()

        updateUiState { copy(isFormValid = isValid) }
    }
}
