package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen

data class RegisterNavActions(
    val navigateToBack: () -> Unit,
    val navigateToLogin: () -> Unit,
) {
    companion object {
        val default = RegisterNavActions(
            navigateToBack = {},
            navigateToLogin = {},
        )
    }
}

fun NavGraphBuilder.register(actions: RegisterNavActions) {
    composable<Screen.Register> {
        val viewModel: RegisterViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect

        RegisterScreen(
            uiState = uiState.value,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            navActions = actions
        )
    }
}