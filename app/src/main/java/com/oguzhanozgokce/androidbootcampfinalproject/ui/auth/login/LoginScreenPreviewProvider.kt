package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginScreenPreviewProvider : PreviewParameterProvider<LoginContract.UiState> {
    override val values: Sequence<LoginContract.UiState>
        get() = sequenceOf(
            // Default state
            LoginContract.UiState(),

            // Loading state
            LoginContract.UiState(
                isLoading = true,
                email = "user@example.com",
                password = "password123"
            ),

            // Filled form state
            LoginContract.UiState(
                email = "user@example.com",
                password = "password123",
                isFormValid = true
            ),

            // Error state
            LoginContract.UiState(
                email = "invalid-email",
                password = "123",
                emailError = "Geçerli bir e-posta adresi girin",
                passwordError = "Şifre en az 6 karakter olmalı"
            )
        )
}