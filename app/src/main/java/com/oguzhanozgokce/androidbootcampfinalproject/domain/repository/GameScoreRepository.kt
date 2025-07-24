package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import kotlinx.coroutines.flow.Flow

interface GameScoreRepository {
    suspend fun saveScore(gameScore: GameScore): Resource<String>
    suspend fun getAllScores(): Resource<List<GameScore>>
    suspend fun getAllScoresByUserId(userId: String): Resource<List<GameScore>>
    suspend fun getTopScores(limit: Int = 10): Resource<List<GameScore>>
    suspend fun getTopScoresByUserId(userId: String, limit: Int = 10): Resource<List<GameScore>>
    suspend fun deleteScore(scoreId: String): Resource<Unit>
    suspend fun clearAllScores(): Resource<Unit>
    suspend fun clearAllScoresByUserId(userId: String): Resource<Unit>
    fun getScoresFlow(): Flow<Resource<List<GameScore>>>
    fun getScoresFlowByUserId(userId: String): Flow<Resource<List<GameScore>>>
}