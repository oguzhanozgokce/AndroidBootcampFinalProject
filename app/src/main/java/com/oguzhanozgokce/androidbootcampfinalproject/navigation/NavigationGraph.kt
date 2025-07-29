package com.oguzhanozgokce.androidbootcampfinalproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oguzhanozgokce.androidbootcampfinalproject.navigation.Screen.*
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.login
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.register
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.gameScore
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.score.ScoreScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.score.ScoreViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.splash.SplashViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.topScores

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
                onAction = viewModel::onAction,
                navActions = SplashNavActions(
                    navigateToLogin = { navController.navigate(Login) },
                    navigateToHome = { navController.navigate(Home) }
                )
            )
        }
        login(
            actions = LoginNavActions(
                navigateToHome = { navController.navigate(Home) },
                navigateToBack = { navController.popBackStack() },
                navigateToSignUp = { navController.navigate(Register) },
                navigateToForgotPassword = { navController.navigate(ForgotPassword) }
            )
        )
        register(
            actions = RegisterNavActions(
                navigateToBack = { navController.popBackStack() },
                navigateToLogin = { navController.navigate(Login) }
            )
        )
        composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            HomeScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateToGameSetup = { navController.navigate(GameSetup) },
                onNavigateToGameScoreboard = { navController.navigate(GameScore) },
                onNavigateToSettings = { navController.navigate(Settings) },
                onNavigateToTopScore = { navController.navigate(TopScores) },
            )
        }
        composable<GameSetup> {
            val viewModel: GameSetupViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            GameSetupScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateToGame = { difficulty, playerName ->
                    navController.navigate(Game(difficulty, playerName))
                },
                onNavigateBack = { navController.popBackStack() }
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
                onAction = viewModel::onAction,
                onNavigationBack = { navController.popBackStack() }
            )
        }
        gameScore(
            actions = GameScoreNavActions(
                navigateToBack = { navController.popBackStack() }
            )
        )
        topScores(
            actions = TopScoresNavActions(
                navigateToBack = { navController.popBackStack() }
            )
        )
    }
}