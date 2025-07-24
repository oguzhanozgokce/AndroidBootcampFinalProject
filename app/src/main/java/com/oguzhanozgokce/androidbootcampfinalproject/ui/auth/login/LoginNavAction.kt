package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen

data class LoginNavActions(
    val navigateToBack: () -> Unit,
) {
    companion object {
        val default = LoginNavActions(
            navigateToBack = {},
        )
    }
}

fun NavGraphBuilder.login(
    actions: LoginNavActions
) {
    composable<Screen.Login> {
        val viewModel: LoginViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect

        LoginScreen(
            uiState = uiState.value,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            navActions = actions
        )
    }
}