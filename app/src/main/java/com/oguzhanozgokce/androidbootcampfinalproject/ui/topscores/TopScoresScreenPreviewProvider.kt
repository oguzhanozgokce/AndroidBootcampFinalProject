package com.oguzhanozgokce.androidbootcampfinalproject.ui.topscores

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore

class TopScoresScreenPreviewProvider : PreviewParameterProvider<TopScoresContract.UiState> {
    override val values: Sequence<TopScoresContract.UiState>
        get() = sequenceOf(
            TopScoresContract.UiState(
                isLoading = true,
                topScores = emptyList(),
            ),
            TopScoresContract.UiState(
                isLoading = false,
                topScores = emptyList(),
            ),
            TopScoresContract.UiState(
                isLoading = false,
                topScores = listOf(
                    GameScore(
                        id = "1",
                        userId = "user1",
                        playerName = "Ahmet Yılmaz",
                        score = 2500,
                        difficulty = GameDifficulty.HARD,
                        gameTime = 125000,
                        completedTime = 125000,
                        timestamp = System.currentTimeMillis(),
                        completed = true
                    ),
                    GameScore(
                        id = "2",
                        userId = "user2",
                        playerName = "Ayşe Demir",
                        score = 2300,
                        difficulty = GameDifficulty.MEDIUM,
                        gameTime = 95000,
                        completedTime = 95000,
                        timestamp = System.currentTimeMillis() - 86400000,
                        completed = true
                    ),
                    GameScore(
                        id = "3",
                        userId = "user3",
                        playerName = "Mehmet Kaya",
                        score = 2100,
                        difficulty = GameDifficulty.EASY,
                        gameTime = 180000,
                        completedTime = 180000,
                        timestamp = System.currentTimeMillis() - 172800000,
                        completed = true
                    )
                )
            ),
        )
}