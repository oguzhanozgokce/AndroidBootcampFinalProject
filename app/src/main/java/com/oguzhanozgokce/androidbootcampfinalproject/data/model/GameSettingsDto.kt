package com.oguzhanozgokce.androidbootcampfinalproject.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSettingsDto(
    val id: String = "game_settings",
    val userId: String? = null,
    val isDarkTheme: Boolean? = null,
    val isTimerEnabled: Boolean? = null,
    val gameTimeLimit: Int? = null,
    val lastPlayerName: String? = null
)