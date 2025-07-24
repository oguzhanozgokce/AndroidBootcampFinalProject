package com.oguzhanozgokce.androidbootcampfinalproject.ui.splash

data class SplashNavActions(
    val navigateToLogin: () -> Unit,
    val navigateToHome: () -> Unit,
) {
    companion object {
        val default = SplashNavActions(
            navigateToLogin = {},
            navigateToHome = {}
        )
    }
}