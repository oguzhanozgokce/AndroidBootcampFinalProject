package com.oguzhanozgokce.androidbootcampfinalproject.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ABButtonVariant {
    PRIMARY,
    SECONDARY,
    OUTLINE,
    TEXT
}

enum class ABButtonSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun ABButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ABButtonVariant = ABButtonVariant.PRIMARY,
    size: ABButtonSize = ABButtonSize.MEDIUM,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.START,
    fullWidth: Boolean = false
) {
    val isPressed by remember { mutableStateOf(false) }

    val colors = getButtonColors(variant)
    val dimensions = getButtonDimensions(size)

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledBackground
            isPressed -> colors.pressedBackground
            else -> colors.backgroundColor
        },
        animationSpec = tween(150),
        label = "backgroundColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledContent
            else -> colors.contentColor
        },
        animationSpec = tween(150),
        label = "contentColor"
    )

    Box(
        modifier = modifier
            .let { if (fullWidth) it.fillMaxWidth() else it }
            .height(dimensions.height)
            .clip(RoundedCornerShape(dimensions.cornerRadius))
            .then(
                when (variant) {
                    ABButtonVariant.OUTLINE -> Modifier
                        .background(backgroundColor)
                        .border(
                            width = 1.dp,
                            color = colors.borderColor ?: contentColor,
                            shape = RoundedCornerShape(dimensions.cornerRadius)
                        )

                    else -> Modifier.background(backgroundColor)
                }
            )
            .clickable(
                enabled = enabled && !loading,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(dimensions.padding),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            // TODO: Add loading indicator
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dimensions.fontSize,
                    color = contentColor
                )
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null && iconPosition == IconPosition.START) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensions.fontSize,
                        color = contentColor
                    ),
                    textAlign = TextAlign.Center,
                )

                if (icon != null && iconPosition == IconPosition.END) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }
            }
        }
    }
}

enum class IconPosition {
    START, END
}

private data class ButtonColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val pressedBackground: Color,
    val disabledBackground: Color,
    val disabledContent: Color,
    val borderColor: Color? = null
)

private data class ButtonDimensions(
    val height: Dp,
    val padding: PaddingValues,
    val cornerRadius: Dp,
    val fontSize: TextUnit,
    val iconSize: Dp
)

@Composable
private fun getButtonColors(variant: ABButtonVariant): ButtonColors {
    return when (variant) {
        ABButtonVariant.PRIMARY -> ButtonColors(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            pressedBackground = MaterialTheme.colorScheme.primaryContainer,
            disabledBackground = MaterialTheme.colorScheme.surfaceVariant,
            disabledContent = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ABButtonVariant.SECONDARY -> ButtonColors(
            backgroundColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            pressedBackground = MaterialTheme.colorScheme.secondaryContainer,
            disabledBackground = MaterialTheme.colorScheme.surfaceVariant,
            disabledContent = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ABButtonVariant.OUTLINE -> ButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            pressedBackground = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
            disabledBackground = Color.Transparent,
            disabledContent = MaterialTheme.colorScheme.onSurfaceVariant,
            borderColor = MaterialTheme.colorScheme.primary
        )

        ABButtonVariant.TEXT -> ButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            pressedBackground = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
            disabledBackground = Color.Transparent,
            disabledContent = MaterialTheme.colorScheme.onSurfaceVariant,
            borderColor = null
        )
    }
}

private fun getButtonDimensions(size: ABButtonSize): ButtonDimensions {
    return when (size) {
        ABButtonSize.SMALL -> ButtonDimensions(
            height = 36.dp,
            padding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            cornerRadius = 8.dp,
            fontSize = 14.sp,
            iconSize = 16.dp
        )

        ABButtonSize.MEDIUM -> ButtonDimensions(
            height = 44.dp,
            padding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            cornerRadius = 10.dp,
            fontSize = 16.sp,
            iconSize = 18.dp
        )

        ABButtonSize.LARGE -> ButtonDimensions(
            height = 52.dp,
            padding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            cornerRadius = 12.dp,
            fontSize = 18.sp,
            iconSize = 20.dp
        )
    }
}