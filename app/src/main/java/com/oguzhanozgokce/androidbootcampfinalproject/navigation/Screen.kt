package com.oguzhanozgokce.androidbootcampfinalproject.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object GameSetup : Screen

    @Serializable
    data object Game : Screen

    @Serializable
    data object Score : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object ForgotPassword : Screen
}