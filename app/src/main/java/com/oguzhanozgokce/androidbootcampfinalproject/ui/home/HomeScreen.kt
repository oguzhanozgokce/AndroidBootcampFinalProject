package com.oguzhanozgokce.androidbootcampfinalproject.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.oguzhanozgokce.androidbootcampfinalproject.R
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButton
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonSize
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonVariant
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABSnackbarType
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.showABSnackbar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.home.HomeContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.MemoryGameTheme
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.StatBestScore
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.StatGamesPlayed
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.StatWinRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateToGameSetup: () -> Unit = {},
    onNavigateToScores: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        snackbarHostState = snackbarHostState,
        collectEffect = { effect ->
            when (effect) {
                is UiEffect.NavigateToGameSetup -> onNavigateToGameSetup()
                is UiEffect.NavigateToScores -> onNavigateToScores()
                is UiEffect.NavigateToSettings -> onNavigateToSettings()
                is UiEffect.NavigateToScoreboard -> onNavigateToScores()
                is UiEffect.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showABSnackbar(
                            message = effect.message,
                            type = ABSnackbarType.ERROR
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Welcome Section
            WelcomeSection(uiState = uiState)

            // Stats Cards
            QuickStatsSection(uiState = uiState)

            // Main Action Buttons
            MainActionsSection(onAction = onAction)

            // Recent Activity or Tips
            RecentActivitySection()
        }
    }
}

@Composable
private fun WelcomeSection(uiState: UiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_puzzle),
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Welcome back!",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = uiState.user?.displayName ?: "Player",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = if (uiState.gamesPlayed > 0) {
                        "You've played ${uiState.gamesPlayed} games!"
                    } else {
                        "Ready for your first challenge?"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
private fun QuickStatsSection(uiState: UiState) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            listOf(
                StatItem(
                    "Games Played",
                    uiState.gamesPlayed.toString(),
                    Icons.Default.PlayArrow,
                    StatGamesPlayed
                ),
                StatItem(
                    "Best Score",
                    uiState.bestScore.toString(),
                    Icons.Default.Star,
                    StatBestScore
                ),
                StatItem(
                    "Win Rate",
                    "${uiState.winRate}%",
                    Icons.Default.Build,
                    StatWinRate
                ),
                StatItem(
                    "Completed",
                    uiState.completedGames.toString(),
                    Icons.Default.PlayArrow,
                    StatGamesPlayed
                ),
                StatItem(
                    "Avg Score",
                    uiState.averageScore.toString(),
                    Icons.Default.Star,
                    StatBestScore
                ),
                StatItem(
                    "Total Score",
                    uiState.totalScore.toString(),
                    Icons.Default.Build,
                    StatWinRate
                )
            )
        ) { stat ->
            StatCard(stat = stat)
        }
    }
}

@Composable
private fun StatCard(stat: StatItem) {
    Card(
        modifier = Modifier.width(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(stat.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = stat.icon,
                    contentDescription = null,
                    tint = stat.color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = stat.value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Text(
                text = stat.label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MainActionsSection(
    onAction: (UiAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        ABButton(
            text = "Start Game",
            onClick = { onAction(UiAction.NavigateToGameSetup) },
            modifier = Modifier
                .fillMaxWidth(),
            variant = ABButtonVariant.PRIMARY,
            size = ABButtonSize.LARGE,
            fullWidth = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ABButton(
                text = "My Scores",
                onClick = { onAction(UiAction.NavigateToScores) },
                modifier = Modifier
                    .weight(1f),
                variant = ABButtonVariant.SECONDARY,
                size = ABButtonSize.LARGE
            )

            ABButton(
                text = "Settings",
                onClick = { onAction(UiAction.NavigateToSettings) },
                modifier = Modifier
                    .weight(1f),
                variant = ABButtonVariant.SECONDARY,
                size = ABButtonSize.LARGE
            )
        }
    }
}

@Composable
private fun RecentActivitySection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Game Tips",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TipItem("💡 Focus on patterns to improve memory")
                TipItem("🎯 Start with easy mode to build confidence")
                TipItem("⚡ Speed improves with practice")
            }
        }
    }
}

@Composable
private fun TipItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

data class StatItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(
    @PreviewParameter(HomeScreenPreviewProvider::class) uiState: UiState,
) {
    MemoryGameTheme {
        HomeScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}