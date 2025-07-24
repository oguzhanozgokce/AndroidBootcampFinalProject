package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

data class GameScore(
    val id: String,
    val userId: String,
    val playerName: String,
    val score: Int,
    val difficulty: GameDifficulty,
    val gameTime: Long,
    val completedTime: Long,
    val timestamp: Long,
    val isCompleted: Boolean
)
