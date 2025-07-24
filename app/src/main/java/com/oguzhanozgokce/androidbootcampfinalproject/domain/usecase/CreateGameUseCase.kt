package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import android.util.Log
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameCard
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameState
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import javax.inject.Inject

class CreateGameUseCase @Inject constructor(
    private val gameScoreRepository: GameScoreRepository
) {

    suspend operator fun invoke(difficulty: GameDifficulty, playerName: String): Result<GameState> {
        return try {
            Log.d("CreateGameUseCase", "Starting game creation for difficulty: $difficulty")

            val numbersResult = gameScoreRepository.getRandomNumbers(difficulty.uniqueNumbers)
            Log.d("CreateGameUseCase", "Got numbers result: ${numbersResult.isSuccess}")

            numbersResult.fold(
                onSuccess = { numbers ->
                    Log.d("CreateGameUseCase", "Numbers received: ${numbers.size} numbers")
                    val gameCards = createGameCards(numbers, difficulty)
                    val gameState = GameState(
                        cards = gameCards,
                        score = 0,
                        timeRemaining = 60,
                        flippedCards = emptyList(),
                        isGameCompleted = false,
                        isGameOver = false,
                        playerName = playerName,
                        difficulty = difficulty,
                        matchedPairs = 0,
                        totalMoves = 0
                    )
                    Log.d("CreateGameUseCase", "Game state created successfully with ${gameCards.size} cards")
                    Result.success(gameState)
                },
                onFailure = { error ->
                    Log.e("CreateGameUseCase", "Failed to get random numbers: ${error.message}")
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Log.e("CreateGameUseCase", "Exception in createGame: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun createGameCards(numbers: List<Int>, difficulty: GameDifficulty): List<GameCard> {
        val cards = mutableListOf<GameCard>()

        numbers.forEach { number ->
            repeat(2) { index ->
                cards.add(
                    GameCard(
                        id = "${number}_$index",
                        number = number,
                        isFlipped = false,
                        isMatched = false,
                        position = 0
                    )
                )
            }
        }

        return cards.shuffled().mapIndexed { index, card ->
            card.copy(position = index)
        }
    }
}