package com.oguzhanozgokce.androidbootcampfinalproject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.Primary

@Composable
fun ABConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Evet",
    cancelText: String = "İptal",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) {
                        Text(
                            text = cancelText,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    TextButton(onClick = onConfirm) {
                        Text(
                            text = confirmText,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ABConfirmationDialogPreview() {
    ABConfirmationDialog(
        title = "Skorları Sıfırla",
        message = "Tüm skorlar silinecek. Bu işlem geri alınamaz. Devam etmek istiyor musunuz?",
        onConfirm = {},
        onCancel = {},
        onDismiss = {}
    )
}