package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamescore

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore

class GameScoreScreenPreviewProvider : PreviewParameterProvider<GameScoreContract.UiState> {
    override val values: Sequence<GameScoreContract.UiState>
        get() = sequenceOf(
            GameScoreContract.UiState(
                isLoading = true,
                gameScores = emptyList(),
                error = null
            ),
            GameScoreContract.UiState(
                isLoading = false,
                gameScores = emptyList(),
                error = null
            ),
            GameScoreContract.UiState(
                isLoading = false,
                gameScores = emptyList(),
                error = "Failed to load game scores"
            ),
            GameScoreContract.UiState(
                isLoading = false,
                gameScores = listOf(
                    GameScore(
                        id = "1",
                        userId = "user1",
                        playerName = "John Doe",
                        score = 2850,
                        difficulty = GameDifficulty.HARD,
                        gameTime = 180000L,
                        completedTime = 175000L,
                        timestamp = System.currentTimeMillis(),
                        completed = true
                    ),
                    GameScore(
                        id = "2",
                        userId = "user1",
                        playerName = "John Doe",
                        score = 1950,
                        difficulty = GameDifficulty.MEDIUM,
                        gameTime = 120000L,
                        completedTime = 118000L,
                        timestamp = System.currentTimeMillis() - 86400000L,
                        completed = true
                    ),
                    GameScore(
                        id = "3",
                        userId = "user1",
                        playerName = "John Doe",
                        score = 1200,
                        difficulty = GameDifficulty.EASY,
                        gameTime = 90000L,
                        completedTime = 0L,
                        timestamp = System.currentTimeMillis() - 172800000L,
                        completed = false
                    ),
                    GameScore(
                        id = "4",
                        userId = "user1",
                        playerName = "John Doe",
                        score = 2650,
                        difficulty = GameDifficulty.HARD,
                        gameTime = 200000L,
                        completedTime = 195000L,
                        timestamp = System.currentTimeMillis() - 259200000L,
                        completed = true
                    ),
                    GameScore(
                        id = "5",
                        userId = "user1",
                        playerName = "John Doe",
                        score = 800,
                        difficulty = GameDifficulty.EASY,
                        gameTime = 60000L,
                        completedTime = 58000L,
                        timestamp = System.currentTimeMillis() - 345600000L,
                        completed = true
                    )
                ),
                error = null
            )
        )
}