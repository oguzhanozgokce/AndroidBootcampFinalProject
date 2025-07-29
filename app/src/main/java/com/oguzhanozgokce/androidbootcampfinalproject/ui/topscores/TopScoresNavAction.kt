package com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen

data class TopScoresNavActions(
    val navigateToBack: () -> Unit,
) {
    companion object {
        val default = TopScoresNavActions(
            navigateToBack = {},
        )
    }
}

fun NavGraphBuilder.topScores(
    actions: TopScoresNavActions
) {
    composable<Screen.TopScores> {
        val viewModel: TopScoresViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect

        TopScoresScreen(
            uiState = uiState.value,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            navActions = actions
        )
    }
}