package com.oguzhanozgokce.androidbootcampfinalproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ABDividerVariant {
    SOLID,
    LIGHT,
    MEDIUM,
    HEAVY
}

enum class ABDividerOrientation {
    HORIZONTAL,
    VERTICAL
}

@Composable
fun ABDivider(
    modifier: Modifier = Modifier,
    variant: ABDividerVariant = ABDividerVariant.LIGHT,
    orientation: ABDividerOrientation = ABDividerOrientation.HORIZONTAL,
    color: Color? = null,
    thickness: Dp? = null
) {
    val dividerColor = color ?: getDividerColor(variant)
    val dividerThickness = thickness ?: getDividerThickness(variant)

    when (orientation) {
        ABDividerOrientation.HORIZONTAL -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(dividerThickness)
                    .background(dividerColor)
            )
        }

        ABDividerOrientation.VERTICAL -> {
            Box(
                modifier = modifier
                    .width(dividerThickness)
                    .fillMaxHeight()
                    .background(dividerColor)
            )
        }
    }
}

@Composable
fun ABDividerWithText(
    text: String,
    modifier: Modifier = Modifier,
    variant: ABDividerVariant = ABDividerVariant.LIGHT,
    textColor: Color? = null,
    dividerColor: Color? = null,
    spacing: Dp = 16.dp
) {
    val finalTextColor = textColor ?: MaterialTheme.colorScheme.onSurfaceVariant
    val finalDividerColor = dividerColor ?: getDividerColor(variant)
    val thickness = getDividerThickness(variant)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(thickness)
                .background(finalDividerColor)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = finalTextColor,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = spacing)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(thickness)
                .background(finalDividerColor)
        )
    }
}

@Composable
private fun getDividerColor(variant: ABDividerVariant): Color {
    return when (variant) {
        ABDividerVariant.SOLID -> MaterialTheme.colorScheme.outline
        ABDividerVariant.LIGHT -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ABDividerVariant.MEDIUM -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        ABDividerVariant.HEAVY -> MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
    }
}

private fun getDividerThickness(variant: ABDividerVariant): Dp {
    return when (variant) {
        ABDividerVariant.SOLID -> 1.dp
        ABDividerVariant.LIGHT -> 0.5.dp
        ABDividerVariant.MEDIUM -> 1.dp
        ABDividerVariant.HEAVY -> 1.5.dp
    }
}