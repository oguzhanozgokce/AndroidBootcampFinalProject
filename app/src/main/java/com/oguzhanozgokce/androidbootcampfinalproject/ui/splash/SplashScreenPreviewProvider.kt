package com.oguzhanozgokce.androidbootcampfinalproject.ui.splash

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SplashScreenPreviewProvider : PreviewParameterProvider<SplashContract.UiState> {
    override val values: Sequence<SplashContract.UiState>
        get() = sequenceOf(
            SplashContract.UiState(
                isLoading = true,
                isCheckingAuth = true
            ),
            SplashContract.UiState(
                isLoading = false,
                isCheckingAuth = false
            ),
            SplashContract.UiState(
                isLoading = true,
                isCheckingAuth = false
            ),
        )
}