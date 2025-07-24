package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

data class GameSettings(
    val userId: String,
    val isDarkTheme: Boolean,
    val isTimerEnabled: Boolean,
    val gameTimeLimit: Int,
    val lastPlayerName: String
)