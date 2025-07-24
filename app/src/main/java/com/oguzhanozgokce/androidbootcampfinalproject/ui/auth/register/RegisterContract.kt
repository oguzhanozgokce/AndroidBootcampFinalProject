
package com.oguzhanozgokce.androidbootcampfinalproject.ui.auth.register

object RegisterContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed class UiAction

    sealed class UiEffect
}