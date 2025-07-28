package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen

data class GameScoreNavActions(
    val navigateToBack: () -> Unit,
) {
    companion object {
        val default = GameScoreNavActions(
            navigateToBack = {},
        )
    }
}

fun NavGraphBuilder.gameScore(
    actions: GameScoreNavActions
) {
    composable<Screen.GameScore> {
        val viewModel: GameScoreViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect

        GameScoreScreen(
            uiState = uiState.value,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            navActions = actions
        )
    }
}