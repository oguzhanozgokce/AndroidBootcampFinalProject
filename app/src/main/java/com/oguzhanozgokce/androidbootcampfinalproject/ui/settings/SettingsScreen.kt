package com.oguzhanozgokce.androidbootcampfinalproject.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButton
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonVariant
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABCenterAlignedTopAppBar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABConfirmationDialog
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABDivider
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABSwitch
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.settings.SettingsContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SettingsScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigationBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = { effect ->
            when (effect) {
                UiEffect.ScoresCleared -> {
                    snackbarHostState.showSnackbar("Skorlar başarıyla temizlendi")
                }

                UiEffect.ThemeUpdated -> {
                    snackbarHostState.showSnackbar("Tema güncellendi")
                }

                UiEffect.TimerUpdated -> {
                    snackbarHostState.showSnackbar("Timer ayarı güncellendi")
                }

                is UiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        },
        snackbarHostState = snackbarHostState,
        topBar = {
            ABCenterAlignedTopAppBar(
                title = "Ayarlar",
                onNavigationClick = onNavigationBack,
            )
        }
    ) { contentPadding ->
        SettingsContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            uiState = uiState,
            onAction = onAction,
        )
    }

    if (uiState.showClearScoresDialog) {
        ABConfirmationDialog(
            title = "Skorları Sıfırla",
            message = "Tüm skorlar silinecek. Bu işlem geri alınamaz. Devam etmek istiyor musunuz?",
            confirmText = "Sıfırla",
            cancelText = "İptal",
            onConfirm = {
                onAction(UiAction.ClearScores)
            },
            onCancel = {
                onAction(UiAction.HideClearScoresDialog)
            },
            onDismiss = {
                onAction(UiAction.HideClearScoresDialog)
            }
        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    val gameSettings = uiState.gameSettings

    if (gameSettings != null) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Görünüm",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ABSwitch(
                title = "Koyu Tema",
                subtitle = "Uygulamayı koyu temada kullan",
                checked = uiState.currentTheme ?: gameSettings.isDarkTheme,
                onCheckedChange = { onAction(UiAction.ToggleTheme(it)) }
            )

            ABDivider()

            Text(
                text = "Oyun Ayarları",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ABSwitch(
                title = "Oyun Süresi",
                subtitle = "Oyunlarda süre sınırı uygula (${gameSettings.gameTimeLimit} saniye)",
                checked = uiState.currentTimer ?: gameSettings.isTimerEnabled,
                onCheckedChange = { onAction(UiAction.ToggleTimer(it)) }
            )

            ABDivider()

            Text(
                text = "Veri",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ABButton(
                text = "Skorları Sıfırla",
                onClick = { onAction(UiAction.ShowClearScoresDialog) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                variant = ABButtonVariant.OUTLINE,
                icon = Icons.Default.Delete
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Ayarlar yükleniyor...",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview(
    @PreviewParameter(SettingsScreenPreviewProvider::class) uiState: UiState,
) {
    SettingsScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigationBack = {}
    )
}