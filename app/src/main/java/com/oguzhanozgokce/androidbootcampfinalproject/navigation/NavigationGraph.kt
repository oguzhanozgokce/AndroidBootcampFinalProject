package com.oguzhanozgokce.androidbootcampfinalproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.Splash
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.Home
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.GameSetup
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.Game
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.Score
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.Settings
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.score.ScoreScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.score.ScoreViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Splash> {
            val viewModel: SplashViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SplashScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            HomeScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<GameSetup> {
            val viewModel: GameSetupViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            GameSetupScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Game> {
            val viewModel: GameViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            GameScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Score> {
            val viewModel: ScoreViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            ScoreScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Settings> {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SettingsScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
    }
}