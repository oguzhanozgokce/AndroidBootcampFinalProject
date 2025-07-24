package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.oguzhanozgokce.androidbootcampfinalproject.R
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register.RegisterContract.UiState
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABBaseScreen
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButton
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonSize
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABButtonVariant
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABSnackbarType
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.ABTextField
import com.oguzhanozgokce.androidbootcampfinalproject.ui.components.showABSnackbar
import com.oguzhanozgokce.androidbootcampfinalproject.ui.theme.MemoryGameTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navActions: RegisterNavActions
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ABBaseScreen(
        modifier = Modifier.fillMaxSize(),
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        snackbarHostState = snackbarHostState,
        collectEffect = { effect ->
            when (effect) {
                is UiEffect.NavigateToLogin -> navActions.navigateToLogin()
                is UiEffect.NavigateToHome -> navActions.navigateToBack()
                is UiEffect.ShowError -> {
                    coroutineScope.launch {
                        snackbarHostState.showABSnackbar(
                            message = effect.message,
                            type = ABSnackbarType.ERROR
                        )
                    }
                }

                is UiEffect.ShowSuccess -> {
                    coroutineScope.launch {
                        snackbarHostState.showABSnackbar(
                            message = effect.message,
                            type = ABSnackbarType.SUCCESS
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        RegisterContent(
            modifier = Modifier
                .fillMaxSize(),
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
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Memory Game",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Join Memory Game now!",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterForm(
                        uiState = uiState,
                        onAction = onAction
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    ABButton(
                        text = "Create Account",
                        onClick = { onAction(UiAction.OnRegisterClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ABButtonVariant.PRIMARY,
                        size = ABButtonSize.LARGE,
                        enabled = uiState.isFormValid && !uiState.isLoading,
                        loading = uiState.isLoading,
                        fullWidth = true
                    )

                    Text(
                        text = "Or Sign up with",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    SocialLoginSection()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            LoginSection(onAction = onAction)
        }
    }
}

@Composable
private fun RegisterForm(
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Display Name",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )

        ABTextField(
            value = uiState.displayName,
            onValueChange = { onAction(UiAction.OnDisplayNameChanged(it)) },
            placeholder = "John Doe",
            leadingIcon = Icons.Default.Person,
            isError = uiState.displayNameError != null,
            errorMessage = uiState.displayNameError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Email",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )

        ABTextField(
            value = uiState.email,
            onValueChange = { onAction(UiAction.OnEmailChanged(it)) },
            placeholder = "johndoe@gmail.com",
            leadingIcon = Icons.Default.Email,
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )

        ABTextField(
            value = uiState.password,
            onValueChange = { onAction(UiAction.OnPasswordChanged(it)) },
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = if (uiState.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SocialLoginSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        SocialButton(
            onClick = { },
            backgroundColor = Color(0xFF1877F2),
            iconRes = R.drawable.ic_launcher_foreground
        )

        SocialButton(
            onClick = { },
            backgroundColor = Color.White,
            iconRes = R.drawable.ic_launcher_foreground
        )

        SocialButton(
            onClick = { },
            backgroundColor = Color.Black,
            iconRes = R.drawable.ic_launcher_foreground
        )
    }
}

@Composable
private fun SocialButton(
    onClick: () -> Unit,
    backgroundColor: Color,
    iconRes: Int
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = if (backgroundColor == Color.White) {
                null
            } else {
                ColorFilter.tint(Color.White)
            }
        )
    }
}

@Composable
private fun LoginSection(
    onAction: (UiAction) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Already have an account? ",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        ABButton(
            text = "Login",
            onClick = { onAction(UiAction.OnLoginClicked) },
            variant = ABButtonVariant.TEXT,
            size = ABButtonSize.MEDIUM
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(
    @PreviewParameter(RegisterScreenPreviewProvider::class) uiState: UiState,
) {
    MemoryGameTheme {
        RegisterScreen(
            uiState = uiState,
            uiEffect = emptyFlow(),
            onAction = {},
            navActions = RegisterNavActions.default
        )
    }
}

