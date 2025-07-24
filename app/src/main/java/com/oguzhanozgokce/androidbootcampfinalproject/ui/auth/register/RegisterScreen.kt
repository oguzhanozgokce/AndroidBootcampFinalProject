package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.finishmarmarab2b.core.common.extension.showToast
import com.oguzhanozgokce.finishmarmarab2b.core.presentation.components.FMBaseScreen
import com.oguzhanozgokce.finishmarmarab2b.ui.theme.FMTheme
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.navigation.RegisterNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun RegisterScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navActions: RegisterNavActions
) {
    val context = LocalContext.current

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = {
            when (it) {
                else -> {}
            }
        },
        topBar = {

        },
    ) { paddingValues ->
        RegisterContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Register Content",
            fontSize = 24.sp,
            color = FMTheme.colors.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(
    @PreviewParameter(RegisterScreenPreviewProvider::class) uiState: UiState,
) {
    FMTheme {
        RegisterScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {},
            navActions = RegisterNavActions.default
        )
    }
}


class RegisterScreenPreviewProvider : PreviewParameterProvider<RegisterContract.UiState> {
    override val values: Sequence<RegisterContract.UiState>
        get() = sequenceOf(
            RegisterContract.UiState(
                isLoading = true,
                list = emptyList(),
            ),
            RegisterContract.UiState(
                isLoading = false,
                list = emptyList(),
            ),
            RegisterContract.UiState(
                isLoading = false,
                list = listOf("Item 1", "Item 2", "Item 3")
            ),
        )
}