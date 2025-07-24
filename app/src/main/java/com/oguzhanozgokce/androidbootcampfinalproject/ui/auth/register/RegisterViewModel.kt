package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            copy(displayName = displayName)
        }
    }

    private fun updateEmail(email: String) {
        updateUiState {
            copy(email = email)
        }
    }

    private fun updatePassword(password: String) {
        updateUiState {
            copy(password = password)
        }
    }

    private fun togglePasswordVisibility() {
        updateUiState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    private fun register() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            val currentUiState = uiState.value

            signUpUseCase(
                email = currentUiState.email,
                password = currentUiState.password,
                displayName = currentUiState.displayName
            ).fold(
                onSuccess = {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowSuccess("Hesap başarıyla oluşturuldu!"))
                    delay(1000)
                    emitUiEffect(UiEffect.NavigateToLogin)
                },
                onFailure = { exception ->
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowError(exception.message ?: "Kayıt sırasında hata oluştu"))
                }
            )
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            emitUiEffect(UiEffect.NavigateToLogin)
        }
    }
}
