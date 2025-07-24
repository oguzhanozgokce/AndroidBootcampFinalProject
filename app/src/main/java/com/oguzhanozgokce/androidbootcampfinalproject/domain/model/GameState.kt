package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

data class GameState(
    val cards: List<GameCard> = emptyList(),
    val score: Int = 0,
    val timeRemaining: Int = 60,
    val flippedCards: List<GameCard> = emptyList(),
    val isGameCompleted: Boolean = false,
    val isGameOver: Boolean = false,
    val playerName: String = "",
    val difficulty: GameDifficulty = GameDifficulty.EASY,
    val matchedPairs: Int = 0,
    val totalMoves: Int = 0
)