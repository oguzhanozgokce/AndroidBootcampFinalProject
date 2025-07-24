package com.oguzhanozgokce.androidbootcampfinalproject.ui.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameCard
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButton
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonSize
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonVariant
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABCenterAlignedTopAppBar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABSnackbarType
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.showABSnackbar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.game.GameContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.CardBackground
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.CardFlipped
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.CardMatched
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.MemoryGameTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateBack: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onAction(UiAction.LoadGame)
    }

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        snackbarHostState = snackbarHostState,
        collectEffect = { effect ->
            when (effect) {
                is UiEffect.NavigateBack -> onNavigateBack()
                is UiEffect.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showABSnackbar(
                            message = effect.message,
                            type = ABSnackbarType.ERROR
                        )
                    }
                }

                is UiEffect.ShowGameCompleteDialog -> {
                }

                is UiEffect.ShowGameOverDialog -> {
                }

                is UiEffect.ShowScoreSaved -> {
                    coroutineScope.launch {
                        snackbarHostState.showABSnackbar(
                            message = "Skorunuz kaydedildi: ${effect.score}",
                            type = ABSnackbarType.SUCCESS
                        )
                    }
                }
            }
        },
        topBar = {
            ABCenterAlignedTopAppBar(
                title = "Hafıza Oyunu",
            )
        }
    ) { contentPadding ->
        GameContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            uiState = uiState,
            onAction = onAction,
        )

        // Game Complete Dialog
        if (uiState.showGameCompleteDialog) {
            GameCompleteDialog(
                score = uiState.score,
                onSaveScore = { onAction(UiAction.OnSaveScore) },
                onRestart = { onAction(UiAction.OnRestartGame) },
                onDismiss = { onAction(UiAction.OnGameCompleteDialogDismiss) }
            )
        }

        // Game Over Dialog
        if (uiState.showGameOverDialog) {
            GameOverDialog(
                onRestart = { onAction(UiAction.OnRestartGame) },
                onBack = { onAction(UiAction.OnBackClicked) },
                onDismiss = { onAction(UiAction.OnGameOverDialogDismiss) }
            )
        }
    }
}

@Composable
fun GameContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Game Stats Header
        GameStatsHeader(
            score = uiState.score,
            timeRemaining = uiState.timeRemaining,
            matchedPairs = uiState.matchedPairs,
            totalPairs = uiState.cards.size / 2
        )

        // Game Grid
        GameGrid(
            cards = uiState.cards,
            difficulty = uiState.difficulty,
            canFlipCards = uiState.canFlipCards,
            onCardClick = { cardId ->
                onAction(UiAction.OnCardClicked(cardId))
            }
        )
    }
}

@Composable
private fun GameStatsHeader(
    score: Int,
    timeRemaining: Int,
    matchedPairs: Int,
    totalPairs: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Score
        StatCard(
            icon = Icons.Filled.Star,
            title = "Skor",
            value = score.toString(),
            color = MaterialTheme.colorScheme.primary
        )

        // Progress
        StatCard(
            icon = null,
            title = "İlerleme",
            value = "$matchedPairs/$totalPairs",
            color = MaterialTheme.colorScheme.secondary
        )

        // Timer
        StatCard(
            icon = null,
            title = "Süre",
            value = formatTime(timeRemaining),
            color = if (timeRemaining <= 10) Color.Red else MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun RowScope.StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
    }
}

@Composable
private fun GameGrid(
    cards: List<GameCard>,
    difficulty: GameDifficulty,
    canFlipCards: Boolean,
    onCardClick: (String) -> Unit
) {
    val cardSize = when (difficulty) {
        GameDifficulty.EASY -> 60.dp
        GameDifficulty.MEDIUM -> 55.dp
        GameDifficulty.HARD -> 50.dp
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(cards) { card ->
            MemoryGameCard(
                card = card,
                canClick = canFlipCards && !card.isFlipped && !card.isMatched,
                onClick = { onCardClick(card.id) },
                width = cardSize,
                height = cardSize
            )
        }
    }
}

@Composable
private fun MemoryGameCard(
    card: GameCard,
    canClick: Boolean,
    onClick: () -> Unit,
    width: Dp,
    height: Dp,
) {
    val density = LocalDensity.current
    val rotationY by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(600),
        label = "card_flip"
    )

    val cardColors = when {
        card.isMatched -> listOf(CardMatched, CardMatched.copy(alpha = 1f))
        card.isFlipped -> listOf(CardFlipped, CardFlipped.copy(alpha = 1f))
        else -> listOf(CardBackground, CardBackground.copy(alpha = 1f))
    }

    val fontSize = when {
        width < 70.dp -> 18.sp
        width < 80.dp -> 20.sp
        else -> 22.sp
    }

    Card(
        modifier = Modifier
            .width(width)
            .height(height)
            .graphicsLayer(
                rotationY = rotationY,
                cameraDistance = 12f * density.density
            )
            .clickable(enabled = canClick) { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (card.isFlipped || card.isMatched) 8.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(cardColors),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                if (rotationY > 90f) {
                    Text(
                        text = card.number.toString(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = fontSize
                        ),
                        modifier = Modifier.graphicsLayer { this.rotationY = 180f }
                    )
                }
            } else {
                if (rotationY < 90f) {
                    Box(
                        modifier = Modifier
                            .size(width / 3)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = fontSize * 0.7f
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameCompleteDialog(
    score: Int,
    onSaveScore: () -> Unit,
    onRestart: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "🎉 Tebrikler!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Oyunu başarıyla tamamladınız!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Skorunuz: $score",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ABButton(
                    text = "Skoru Kaydet",
                    onClick = onSaveScore,
                    variant = ABButtonVariant.PRIMARY,
                    size = ABButtonSize.MEDIUM
                )
                ABButton(
                    text = "Tekrar Oyna",
                    onClick = onRestart,
                    variant = ABButtonVariant.SECONDARY,
                    size = ABButtonSize.MEDIUM
                )
            }
        }
    )
}

@Composable
private fun GameOverDialog(
    onRestart: () -> Unit,
    onBack: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "⏰ Süre Bitti!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Maalesef süreniz doldu. Tekrar denemek ister misiniz?",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ABButton(
                    modifier = Modifier.weight(1f),
                    text = "Tekrar Dene",
                    onClick = onRestart,
                    variant = ABButtonVariant.PRIMARY,
                    size = ABButtonSize.MEDIUM,
                    fullWidth = true
                )
                ABButton(
                    modifier = Modifier.weight(1f),
                    text = "Geri Dön",
                    onClick = onBack,
                    variant = ABButtonVariant.SECONDARY,
                    size = ABButtonSize.MEDIUM,
                    fullWidth = true
                )
            }
        }
    )
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview(
    @PreviewParameter(GameScreenPreviewProvider::class) uiState: UiState,
) {
    MemoryGameTheme {
        GameScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}