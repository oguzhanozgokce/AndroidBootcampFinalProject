package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.SignUpUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.OnDisplayNameChanged -> updateDisplayName(uiAction.displayName)
            is UiAction.OnEmailChanged -> updateEmail(uiAction.email)
            is UiAction.OnPasswordChanged -> updatePassword(uiAction.password)
            is UiAction.OnPasswordVisibilityToggled -> togglePasswordVisibility()
            is UiAction.OnRegisterClicked -> register()
            is UiAction.OnLoginClicked -> navigateToLogin()
        }
    }

    private fun updateDisplayName(displayName: String) {
        updateUiState {
            copy(
                displayName = displayName,
                displayNameError = validateDisplayName(displayName),
                isFormValid = isFormValid(
                    displayName = displayName,
                    email = uiState.value.email,
                    password = uiState.value.password
                )
            )
        }
    }

    private fun updateEmail(email: String) {
        updateUiState {
            copy(
                email = email,
                emailError = validateEmail(email),
                isFormValid = isFormValid(
                    displayName = uiState.value.displayName,
                    email = email,
                    password = uiState.value.password
                )
            )
        }
    }

    private fun updatePassword(password: String) {
        updateUiState {
            copy(
                password = password,
                passwordError = validatePassword(password),
                isFormValid = isFormValid(
                    displayName = uiState.value.displayName,
                    email = uiState.value.email,
                    password = password
                )
            )
        }
    }

    private fun togglePasswordVisibility() {
        updateUiState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    private fun register() {
        if (!uiState.value.isFormValid) return

        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            try {
                val currentUiState = uiState.value

                when (val result = signUpUseCase(
                    email = currentUiState.email,
                    password = currentUiState.password,
                    displayName = currentUiState.displayName
                )) {
                    is Resource.Success -> {
                        emitUiEffect(UiEffect.ShowSuccess("Hesap başarıyla oluşturuldu!"))
                        delay(1000)
                        emitUiEffect(UiEffect.NavigateToLogin)
                    }
                    is Resource.Error -> {
                        emitUiEffect(UiEffect.ShowError(result.message ?: "Kayıt sırasında hata oluştu"))
                    }
                }
            } catch (e: Exception) {
                emitUiEffect(UiEffect.ShowError("Beklenmeyen bir hata oluştu: ${e.message}"))
            } finally {
                updateUiState { copy(isLoading = false) }
            }
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            emitUiEffect(UiEffect.NavigateToLogin)
        }
    }

    private fun validateDisplayName(displayName: String): String? {
        return when {
            displayName.isBlank() -> "İsim alanı zorunludur"
            displayName.length < 2 -> "İsim en az 2 karakter olmalıdır"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "E-posta alanı zorunludur"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Geçerli bir e-posta adresi girin"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Şifre alanı zorunludur"
            password.length < 6 -> "Şifre en az 6 karakter olmalıdır"
            else -> null
        }
    }

    private fun isFormValid(
        displayName: String,
        email: String,
        password: String
    ): Boolean {
        return validateDisplayName(displayName) == null &&
                validateEmail(email) == null &&
                validatePassword(password) == null
    }
}
