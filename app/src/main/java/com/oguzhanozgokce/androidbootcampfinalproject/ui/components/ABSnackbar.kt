package com.oguzhanozgokce.androidbootcampfinalproject.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class ABSnackbarType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

data class ABSnackbarVisuals(
    val message: String,
    val type: ABSnackbarType = ABSnackbarType.INFO,
    val title: String? = null,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = true,
    val duration: Long = 4000L
)

@Composable
fun ABSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    val visuals = snackbarData.visuals
    val type = when {
        visuals.message.contains("başarı", ignoreCase = true) -> ABSnackbarType.SUCCESS
        visuals.message.contains("hata", ignoreCase = true) -> ABSnackbarType.ERROR
        visuals.message.contains("uyarı", ignoreCase = true) -> ABSnackbarType.WARNING
        else -> ABSnackbarType.INFO
    }

    ABSnackbarContent(
        message = visuals.message,
        type = type,
        actionLabel = visuals.actionLabel,
        onActionClick = snackbarData::performAction,
        onDismiss = snackbarData::dismiss,
        modifier = modifier
    )
}

@Composable
fun ABSnackbarContent(
    message: String,
    modifier: Modifier = Modifier,
    type: ABSnackbarType = ABSnackbarType.INFO,
    title: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    withDismissAction: Boolean = true
) {
    val colors = getSnackbarColors(type)
    val icon = getSnackbarIcon(type)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .clip(RoundedCornerShape(8.dp))
            .background(colors.backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.iconColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.messageColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (withDismissAction) {
            onDismiss?.let { dismiss ->
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = dismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = colors.dismissColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ABSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            ABSnackbar(snackbarData = snackbarData)
        }
    )
}

@Composable
fun ABFloatingSnackbar(
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    type: ABSnackbarType = ABSnackbarType.INFO,
    title: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    duration: Long = 4000L,
    withDismissAction: Boolean = true
) {
    LaunchedEffect(isVisible) {
        if (isVisible && duration > 0) {
            delay(duration)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(250)
        ) + fadeOut(animationSpec = tween(250)),
        modifier = modifier
    ) {
        ABSnackbarContent(
            message = message,
            type = type,
            title = title,
            actionLabel = actionLabel,
            onActionClick = onActionClick,
            onDismiss = onDismiss,
            withDismissAction = withDismissAction
        )
    }
}

// Helper extension function to show snackbar easily
suspend fun SnackbarHostState.showABSnackbar(
    message: String,
    type: ABSnackbarType = ABSnackbarType.INFO,
    actionLabel: String? = null,
    withDismissAction: Boolean = true
) {
    showSnackbar(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = withDismissAction
    )
}

@Composable
private fun getSnackbarColors(type: ABSnackbarType): SnackbarColors {
    return when (type) {
        ABSnackbarType.SUCCESS -> SnackbarColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            iconColor = Color(0xFF4CAF50),
            titleColor = MaterialTheme.colorScheme.onSurface,
            messageColor = MaterialTheme.colorScheme.onSurface,
            dismissColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ABSnackbarType.ERROR -> SnackbarColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            iconColor = Color(0xFFF44336),
            titleColor = MaterialTheme.colorScheme.onSurface,
            messageColor = MaterialTheme.colorScheme.onSurface,
            dismissColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ABSnackbarType.WARNING -> SnackbarColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            iconColor = Color(0xFFFF9800),
            titleColor = MaterialTheme.colorScheme.onSurface,
            messageColor = MaterialTheme.colorScheme.onSurface,
            dismissColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ABSnackbarType.INFO -> SnackbarColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            iconColor = Color(0xFF2196F3),
            titleColor = MaterialTheme.colorScheme.onSurface,
            messageColor = MaterialTheme.colorScheme.onSurface,
            dismissColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private data class SnackbarColors(
    val backgroundColor: Color,
    val iconColor: Color,
    val titleColor: Color,
    val messageColor: Color,
    val dismissColor: Color
)

private fun getSnackbarIcon(type: ABSnackbarType): ImageVector {
    return when (type) {
        ABSnackbarType.SUCCESS -> Icons.Default.CheckCircle
        ABSnackbarType.ERROR -> Icons.Default.Close
        ABSnackbarType.WARNING -> Icons.Default.Warning
        ABSnackbarType.INFO -> Icons.Default.Info
    }
}