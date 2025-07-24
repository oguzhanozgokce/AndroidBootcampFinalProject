package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButton
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonSize
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonVariant
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABCenterAlignedTopAppBar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABSnackbarType
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.showABSnackbar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.MemoryGameTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun GameSetupScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateToGame: (GameDifficulty) -> Unit = {},
    onNavigateBack: () -> Unit = {}
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
                is UiEffect.NavigateToGame -> onNavigateToGame(effect.difficulty)
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
        topBar = {
            ABCenterAlignedTopAppBar(
                title = "Oyun Kurulumu",
                onNavigationClick = onNavigateBack
            )
        },
        bottomBar = {
            ABButton(
                text = "Oyuna Başla",
                onClick = { onAction(UiAction.OnStartGameClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                variant = ABButtonVariant.PRIMARY,
                size = ABButtonSize.LARGE,
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        GameSetupContent(
            modifier = Modifier.padding(contentPadding),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
fun GameSetupContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    Column(
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header Section
        HeaderSection()

        DifficultySelectionSection(
            selectedDifficulty = uiState.selectedDifficulty,
            difficulties = uiState.availableDifficulties,
            onDifficultySelected = { difficulty ->
                onAction(UiAction.OnDifficultyChanged(difficulty))
            }
        )

        GameInfoSection(
            selectedDifficulty = uiState.availableDifficulties.find {
                it.level == uiState.selectedDifficulty
            } ?: uiState.availableDifficulties.first()
        )
        GameRulesSection()
    }
}

@Composable
private fun HeaderSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🧠",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 48.dp.value.toInt().sp
            )
            Text(
                text = "Hafıza Oyunu",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "Zihnini test et ve hafızanı geliştir!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DifficultySelectionSection(
    selectedDifficulty: GameDifficulty,
    difficulties: List<DifficultyInfo>,
    onDifficultySelected: (GameDifficulty) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Zorluk Seviyesi Seç",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(difficulties) { difficulty ->
                DifficultyCard(
                    difficulty = difficulty,
                    isSelected = difficulty.level == selectedDifficulty,
                    onClick = { onDifficultySelected(difficulty.level) }
                )
            }
        }
    }
}

@Composable
private fun DifficultyCard(
    difficulty: DifficultyInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardColor = when (difficulty.level) {
        GameDifficulty.EASY -> Color(0xFF4CAF50)
        GameDifficulty.MEDIUM -> Color(0xFFFF9800)
        GameDifficulty.HARD -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier
            .size(140.dp, 180.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        3.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(cardColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = cardColor,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = difficulty.level.cardCount.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = cardColor
                        )
                    )
                }
            }

            Text(
                text = difficulty.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Text(
                text = "${difficulty.level.cardCount} Kart",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Text(
                text = difficulty.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GameInfoSection(selectedDifficulty: DifficultyInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                    text = "Oyun Bilgileri",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GameInfoItem(
                    icon = "🃏",
                    title = "Kart Sayısı",
                    description = "${selectedDifficulty.level.cardCount} kart (${selectedDifficulty.level.cardCount / 2} çift)"
                )

                GameInfoItem(
                    icon = "⏱️",
                    title = "Süre",
                    description = "60 saniye"
                )

                GameInfoItem(
                    icon = "🎯",
                    title = "Hedef",
                    description = "Tüm kartları süre bitmeden eşleştir"
                )
            }
        }
    }
}

@Composable
private fun GameRulesSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
            Text(
                text = "Nasıl Oynanır?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RuleItem(
                    number = "1",
                    text = "Kartları çevir ve altındaki sayıları hatırla"
                )

                RuleItem(
                    number = "2",
                    text = "Aynı sayıları bulup eşleştir"
                )

                RuleItem(
                    number = "3",
                    text = "Eşleşen kartlar yeşile döner ve açık kalır"
                )

                RuleItem(
                    number = "4",
                    text = "Hızlı ve doğru eşleştirme ile daha fazla puan kazan!"
                )
            }
        }
    }
}

@Composable
private fun GameInfoItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun RuleItem(
    number: String,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameSetupScreenPreview(
    @PreviewParameter(GameSetupScreenPreviewProvider::class) uiState: UiState,
) {
    MemoryGameTheme {
        GameSetupScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {}
        )
    }
}