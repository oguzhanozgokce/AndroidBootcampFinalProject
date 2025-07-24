package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

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
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun GameSetupScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
) {
    uiEffect.collectWithLifecycle {}

    GameSetupContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = onAction,
    )
}

@Composable
fun GameSetupContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "GameSetup Content",
            fontSize = 24.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameSetupScreenPreview(
    @PreviewParameter(GameSetupScreenPreviewProvider::class) uiState: UiState,
) {
    GameSetupScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
    )
}