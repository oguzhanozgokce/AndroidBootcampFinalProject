package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import kotlinx.coroutines.flow.Flow

interface GameScoreRepository {
    suspend fun saveScore(gameScore: GameScore): Result<String>
    suspend fun getAllScores(): Result<List<GameScore>>
    suspend fun getAllScoresByUserId(userId: String): Result<List<GameScore>>
    suspend fun getTopScores(limit: Int = 10): Result<List<GameScore>>
    suspend fun getTopScoresByUserId(userId: String, limit: Int = 10): Result<List<GameScore>>
    suspend fun deleteScore(scoreId: String): Result<Unit>
    suspend fun clearAllScores(): Result<Unit>
    suspend fun clearAllScoresByUserId(userId: String): Result<Unit>
    fun getScoresFlow(): Flow<Result<List<GameScore>>>
    fun getScoresFlowByUserId(userId: String): Flow<Result<List<GameScore>>>
    suspend fun getRandomNumbers(count: Int): Result<List<Int>>
}