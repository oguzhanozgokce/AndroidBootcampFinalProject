package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import javax.inject.Inject

data class GameStats(
    val gamesPlayed: Int,
    val bestScore: Int,
    val winRate: Int,
    val totalScore: Int,
    val completedGames: Int
)

class GetGameStatsUseCase @Inject constructor(
    private val gameScoreRepository: GameScoreRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<GameStats> {
        return try {
            val userResult = authRepository.getCurrentUser()

            userResult.fold(
                onSuccess = { user ->
                    if (user != null) {
                        val scoresResult = gameScoreRepository.getAllScoresByUserId(user.uid)

                        scoresResult.fold(
                            onSuccess = { scores ->
                                val stats = calculateGameStats(scores)
                                Result.success(stats)
                            },
                            onFailure = { error ->
                                Result.failure(error)
                            }
                        )
                    } else {
                        Result.success(GameStats(0, 0, 0, 0, 0))
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateGameStats(scores: List<GameScore>): GameStats {
        val gamesPlayed = scores.size
        val bestScore = scores.maxOfOrNull { it.score } ?: 0
        val completedGames = scores.count { it.isCompleted }
        val totalScore = scores.sumOf { it.score }
        val winRate = if (gamesPlayed > 0) {
            (completedGames * 100) / gamesPlayed
        } else {
            0
        }

        return GameStats(
            gamesPlayed = gamesPlayed,
            bestScore = bestScore,
            winRate = winRate,
            totalScore = totalScore,
            completedGames = completedGames
        )
    }
}