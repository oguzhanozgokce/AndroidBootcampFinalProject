package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class RegisterScreenPreviewProvider : PreviewParameterProvider<RegisterContract.UiState> {
    override val values: Sequence<RegisterContract.UiState>
        get() = sequenceOf(
            RegisterContract.UiState(
                isLoading = false,
                displayName = "",
                email = "",
                password = "",
                isFormValid = false
            ),
            RegisterContract.UiState(
                isLoading = false,
                displayName = "John Doe",
                email = "john@example.com",
                password = "123456",
                isFormValid = true
            ),
            RegisterContract.UiState(
                isLoading = true,
                displayName = "John Doe",
                email = "john@example.com",
                password = "123456",
                isFormValid = true
            ),
        )
}