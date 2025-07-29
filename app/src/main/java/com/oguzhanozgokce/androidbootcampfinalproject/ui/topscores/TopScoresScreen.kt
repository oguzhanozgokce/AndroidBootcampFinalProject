package com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABCenterAlignedTopAppBar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.MemoryGameTheme
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankBronze
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankBronzeBackground
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankBronzeBackgroundDark
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankDefault
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankGold
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankGoldBackground
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankGoldBackgroundDark
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankSilver
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankSilverBackground
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.RankSilverBackgroundDark
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores.TopScoresContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScoresScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navActions: TopScoresNavActions
) {

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = {
            when (it) {
                is UiEffect.ShowError -> {
                    // Handle error display if needed
                }
            }
        },
        topBar = {
            ABCenterAlignedTopAppBar(
                title = "En Yüksek Skorlar",
                onNavigationClick = navActions.navigateToBack,
                actions = {
                    IconButton(onClick = { onAction(UiAction.Refresh) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Yenile"
                        )
                    }
                },

                )
        },
    ) { paddingValues ->
        TopScoresContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
fun TopScoresContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    if (uiState.topScores.isEmpty() && !uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Henüz skor bulunmuyor",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(uiState.topScores) { index, score ->
                TopScoreItem(
                    rank = index + 1,
                    score = score
                )
            }
        }
    }
}

@Composable
fun TopScoreItem(
    rank: Int,
    score: GameScore
) {
    val rankColor = when (rank) {
        1 -> RankGold
        2 -> RankSilver
        3 -> RankBronze
        else -> RankDefault
    }

    val cardBackgroundColor = when (rank) {
        1 -> MaterialTheme.colorScheme.surfaceContainer
        2 -> MaterialTheme.colorScheme.surfaceContainerHigh
        3 -> MaterialTheme.colorScheme.surfaceContainerHighest
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
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
                    .background(rankColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Player info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = score.playerName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatGameTime(score.gameTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = score.difficulty.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = formatDate(score.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            // Score
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${score.score}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "puan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatGameTime(timeInMillis: Long): String {
    val seconds = timeInMillis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun TopScoresScreenPreview(
    @PreviewParameter(TopScoresScreenPreviewProvider::class) uiState: UiState,
) {
    MemoryGameTheme {
        TopScoresScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {},
            navActions = TopScoresNavActions.default
        )
    }
}
