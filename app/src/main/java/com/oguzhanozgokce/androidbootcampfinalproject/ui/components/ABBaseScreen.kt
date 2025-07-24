package com.oguzhanozgokce.androidbootcampfinalproject.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.oguzhanozgokce.androidbootcampfinalproject.common.collectWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> ABBaseScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    uiEffect: Flow<T>? = null,
    collectEffect: (suspend (T) -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit,
) {
    uiEffect?.collectWithLifecycle {
        collectEffect?.invoke(it)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = {
            snackbarHostState?.let { hostState ->
                ABSnackbarHost(hostState = hostState)
            }
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        content = { paddingValues ->
            content(paddingValues)
            if (isLoading) {
                ABLoading()
            }
        }
    )
}