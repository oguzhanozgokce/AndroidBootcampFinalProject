package com.oguzhanozgokce.androidbootcampfinalproject.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.androidbootcampfinalproject.common.collectWithLifecycle
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
) {
    uiEffect.collectWithLifecycle {}

    SettingsContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = onAction,
    )
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Settings Content",
            fontSize = 24.sp,
        )
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
    )
}