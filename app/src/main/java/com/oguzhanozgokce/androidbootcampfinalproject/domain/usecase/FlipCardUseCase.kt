package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.calculateScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameState
import javax.inject.Inject

class FlipCardUseCase @Inject constructor() {

    operator fun invoke(gameState: GameState, cardId: String): GameState {
        val cardToFlip = gameState.cards.find { it.id == cardId }
            ?: return gameState

        // Kart zaten açık veya eşleşmiş ise işlem yapma
        if (cardToFlip.isFlipped || cardToFlip.isMatched) {
            return gameState
        }

        // İki kart açık ise işlem yapma
        if (gameState.flippedCards.size >= 2) {
            return gameState
        }

        val updatedCards = gameState.cards.map { card ->
            if (card.id == cardId) {
                card.copy(isFlipped = true)
            } else {
                card
            }
        }

        val newFlippedCards = gameState.flippedCards + cardToFlip.copy(isFlipped = true)
        val newTotalMoves = gameState.totalMoves + 1

        // İki kart açıldıysa eşleşme kontrolü
        if (newFlippedCards.size == 2) {
            val firstCard = newFlippedCards[0]
            val secondCard = newFlippedCards[1]

            if (firstCard.number == secondCard.number) {
                // Eşleşme var
                val matchedCards = updatedCards.map { card ->
                    if (card.id == firstCard.id || card.id == secondCard.id) {
                        card.copy(isMatched = true, isFlipped = true)
                    } else {
                        card
                    }
                }

                val newMatchedPairs = gameState.matchedPairs + 1
                val newScore = calculateScore(
                    matchedPairs = newMatchedPairs,
                    timeUsed = 60L - gameState.timeRemaining,
                    difficulty = gameState.difficulty,
                    totalTime = 60
                )

                val isGameCompleted = newMatchedPairs == gameState.difficulty.uniqueNumbers

                return gameState.copy(
                    cards = matchedCards,
                    flippedCards = emptyList(),
                    matchedPairs = newMatchedPairs,
                    score = newScore,
                    totalMoves = newTotalMoves,
                    isGameCompleted = isGameCompleted
                )
            }
        }

        return gameState.copy(
            cards = updatedCards,
            flippedCards = newFlippedCards,
            totalMoves = newTotalMoves
        )
    }

    fun closeUnmatchedCards(gameState: GameState): GameState {
        val updatedCards = gameState.cards.map { card ->
            if (card.isFlipped && !card.isMatched) {
                card.copy(isFlipped = false)
            } else {
                card
            }
        }

        return gameState.copy(
            cards = updatedCards,
            flippedCards = emptyList()
        )
    }
}