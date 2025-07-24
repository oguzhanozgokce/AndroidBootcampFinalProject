package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login

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
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.LoginContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.login.navigation.LoginNavActions
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navActions: LoginNavActions
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
        LoginContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Login Content",
            fontSize = 24.sp,
            color = FMTheme.colors.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginScreenPreviewProvider::class) uiState: UiState,
) {
    FMTheme {
        LoginScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {},
            navActions = LoginNavActions.default
        )
    }
}


class LoginScreenPreviewProvider : PreviewParameterProvider<LoginContract.UiState> {
    override val values: Sequence<LoginContract.UiState>
        get() = sequenceOf(
            LoginContract.UiState(
                isLoading = true,
                list = emptyList(),
            ),
            LoginContract.UiState(
                isLoading = false,
                list = emptyList(),
            ),
            LoginContract.UiState(
                isLoading = false,
                list = listOf("Item 1", "Item 2", "Item 3")
            ),
        )
}