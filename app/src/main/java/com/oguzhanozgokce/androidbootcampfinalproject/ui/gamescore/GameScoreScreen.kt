package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.androidbootcampfinalproject.R.drawable.ic_time
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABCenterAlignedTopAppBar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore.GameScoreContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.StatBestScore
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.StatGamesPlayed
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.StatWinRate
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScoreScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navActions: GameScoreNavActions
) {
    LocalContext.current

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = {
            when (it) {
                is UiEffect.ShowError -> {
                    // Handle error
                }
            }
        },
        topBar = {
            ABCenterAlignedTopAppBar(
                title = "Game Statistics",
                onNavigationClick = { navActions.navigateToBack() },
                actions = {
                    IconButton(onClick = { onAction(UiAction.RefreshScores) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        GameScoreContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
fun GameScoreContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.gameScores) {
        isVisible = true
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Statistics Overview Section
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(600)
                )
            ) {
                StatisticsOverview(gameScores = uiState.gameScores)
            }
        }

        // Recent Games Header
        if (uiState.gameScores.isNotEmpty()) {
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 200))
                ) {
                    Text(
                        text = "Recent Games",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Game Scores List
            itemsIndexed(uiState.gameScores.take(10)) { index, score ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(400, delayMillis = 300 + index * 100)) +
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400, delayMillis = 300 + index * 100)
                        )
                ) {
                    CompactScoreItem(score = score, rank = index + 1)
                }
            }
        } else {
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 400))
                ) {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
fun StatisticsOverview(gameScores: List<GameScore>) {
    val totalGames = gameScores.size
    val bestScore = gameScores.maxOfOrNull { it.score } ?: 0
    val completedGames = gameScores.count { it.completed }
    val winRate = if (totalGames > 0) (completedGames * 100) / totalGames else 0
    val avgTime = if (gameScores.isNotEmpty()) {
        gameScores.filter { it.completed }.map { it.completedTime }.average().toInt()
    } else {
        0
    }

    Column {
        Text(
            text = "Statistics Overview",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Main Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompactStatCard(
                modifier = Modifier.weight(1f),
                title = "Games",
                value = totalGames.toString(),
                icon = Icons.Default.PlayArrow,
                color = StatGamesPlayed
            )

            CompactStatCard(
                modifier = Modifier.weight(1f),
                title = "Best Score",
                value = bestScore.toString(),
                icon = Icons.Default.Info,
                color = StatBestScore
            )

            CompactStatCard(
                modifier = Modifier.weight(1f),
                title = "Win Rate",
                value = "$winRate%",
                icon = Icons.Default.Star,
                color = StatWinRate
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Win Rate Progress
        if (totalGames > 0) {
            CompactWinRateCard(winRate = winRate, avgTime = avgTime)
        }
    }
}

@Composable
fun CompactStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    var animatedValue by remember { mutableStateOf(0f) }
    val targetValue = value.replace("%", "").toFloatOrNull() ?: 0f

    val animatedProgress by animateFloatAsState(
        targetValue = animatedValue,
        animationSpec = tween(800),
        label = "stat_animation"
    )

    LaunchedEffect(targetValue) {
        animatedValue = targetValue
    }

    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = if (value.contains("%")) {
                        "${animatedProgress.toInt()}%"
                    } else {
                        animatedProgress.toInt()
                            .toString()
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CompactWinRateCard(winRate: Int, avgTime: Int) {
    var animatedProgress by remember { mutableStateOf(0f) }

    val progress by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(1000),
        label = "circle_progress"
    )

    LaunchedEffect(winRate) {
        animatedProgress = winRate / 100f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress Circle
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(80.dp)) {
                    drawCompactCircularProgress(progress, winRate)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StatWinRate
                    )
                    Text(
                        text = "Win Rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Additional Stats
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(ic_time),
                        contentDescription = "Average Time",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Avg Time: ${avgTime}s",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Performance",
                        modifier = Modifier.size(16.dp),
                        tint = StatBestScore
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            winRate >= 80 -> "Excellent"
                            winRate >= 60 -> "Good"
                            winRate >= 40 -> "Average"
                            else -> "Keep trying!"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

fun DrawScope.drawCompactCircularProgress(progress: Float, winRate: Int) {
    val strokeWidth = 6.dp.toPx()
    val radius = (size.minDimension - strokeWidth) / 2
    val center = Offset(size.width / 2, size.height / 2)

    // Background circle
    drawCircle(
        color = Color.Gray.copy(alpha = 0.2f),
        radius = radius,
        center = center,
        style = Stroke(width = strokeWidth)
    )

    // Progress arc
    val sweepAngle = 360f * progress
    val color = when {
        winRate >= 80 -> StatGamesPlayed
        winRate >= 60 -> StatBestScore
        winRate >= 40 -> StatWinRate
        else -> Color(0xFFFF5722)
    }

    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = strokeWidth),
        size = Size(radius * 2, radius * 2),
        topLeft = Offset(center.x - radius, center.y - radius)
    )
}

@Composable
fun CompactScoreItem(score: GameScore, rank: Int) {
    remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> MaterialTheme.colorScheme.primary
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = score.playerName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${score.score} pts",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = StatBestScore
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = score.difficulty.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            color = when (score.difficulty) {
                                GameDifficulty.EASY -> StatGamesPlayed
                                GameDifficulty.MEDIUM -> StatBestScore
                                GameDifficulty.HARD -> Color(0xFFFF5722)
                            },
                            modifier = Modifier
                                .background(
                                    when (score.difficulty) {
                                        GameDifficulty.EASY -> StatGamesPlayed.copy(alpha = 0.1f)
                                        GameDifficulty.MEDIUM -> StatBestScore.copy(alpha = 0.1f)
                                        GameDifficulty.HARD -> Color(0xFFFF5722).copy(alpha = 0.1f)
                                    },
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (score.completed) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Completed",
                                modifier = Modifier.size(12.dp),
                                tint = StatGamesPlayed
                            )
                        }
                    }

                    Text(
                        text = "${score.gameTime}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "No games",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No games played yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start playing to see your statistics here!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
