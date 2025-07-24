package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.createGameCards
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameState
import javax.inject.Inject

class CreateGameUseCase @Inject constructor() {

    operator fun invoke(
        playerName: String,
        difficulty: GameDifficulty,
        gameTimeLimit: Int = 60
    ): GameState {
        val cards = createGameCards(difficulty)

        return GameState(
            cards = cards,
            score = 0,
            timeRemaining = gameTimeLimit,
            flippedCards = emptyList(),
            isGameCompleted = false,
            isGameOver = false,
            playerName = playerName,
            difficulty = difficulty,
            matchedPairs = 0,
            totalMoves = 0
        )
    }
}