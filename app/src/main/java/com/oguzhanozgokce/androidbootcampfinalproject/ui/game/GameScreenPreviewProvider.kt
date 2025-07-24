package com.oguzhanozgokce.androidbootcampfinalproject.ui.game

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameCard
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameResult

class GameScreenPreviewProvider : PreviewParameterProvider<GameContract.UiState> {
    override val values: Sequence<GameContract.UiState>
        get() = sequenceOf(
            // Loading state
            GameContract.UiState(
                isLoading = true,
                difficulty = GameDifficulty.EASY
            ),
            // Game in progress - Easy
            GameContract.UiState(
                isLoading = false,
                difficulty = GameDifficulty.EASY,
                cards = createPreviewCards(GameDifficulty.EASY),
                score = 150,
                timeRemaining = 45,
                matchedPairs = 2,
                totalMoves = 8,
                canFlipCards = true
            ),
            // Game in progress - Medium with some flipped cards
            GameContract.UiState(
                isLoading = false,
                difficulty = GameDifficulty.MEDIUM,
                cards = createPreviewCardsWithFlipped(GameDifficulty.MEDIUM),
                score = 280,
                timeRemaining = 23,
                matchedPairs = 4,
                totalMoves = 15,
                canFlipCards = false // Cards are being processed
            ),
            // Game completed
            GameContract.UiState(
                isLoading = false,
                difficulty = GameDifficulty.HARD,
                cards = createPreviewCardsAllMatched(GameDifficulty.HARD),
                score = 500,
                timeRemaining = 12,
                matchedPairs = 10,
                totalMoves = 25,
                isGameCompleted = true,
                gameResult = GameResult.Completed(500, 48),
                showGameCompleteDialog = true
            ),
            // Game over
            GameContract.UiState(
                isLoading = false,
                difficulty = GameDifficulty.MEDIUM,
                cards = createPreviewCards(GameDifficulty.MEDIUM),
                score = 120,
                timeRemaining = 0,
                matchedPairs = 3,
                totalMoves = 20,
                isGameOver = true,
                gameResult = GameResult.TimeUp,
                showGameOverDialog = true
            )
        )

    private fun createPreviewCards(difficulty: GameDifficulty): List<GameCard> {
        val cardCount = difficulty.cardCount
        return (0 until cardCount).map { index ->
            GameCard(
                id = "card_$index",
                number = (index / 2) + 1,
                isFlipped = false,
                isMatched = false,
                position = index
            )
        }
    }

    private fun createPreviewCardsWithFlipped(difficulty: GameDifficulty): List<GameCard> {
        val cards = createPreviewCards(difficulty).toMutableList()
        cards[0] = cards[0].copy(isFlipped = true)
        cards[1] = cards[1].copy(isFlipped = true)
        cards[2] = cards[2].copy(isMatched = true)
        cards[3] = cards[3].copy(isMatched = true)
        return cards
    }

    private fun createPreviewCardsAllMatched(difficulty: GameDifficulty): List<GameCard> {
        return createPreviewCards(difficulty).map { card ->
            card.copy(isMatched = true)
        }
    }
}