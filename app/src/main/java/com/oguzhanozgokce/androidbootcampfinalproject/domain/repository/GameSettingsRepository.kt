package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import kotlinx.coroutines.flow.Flow

interface GameSettingsRepository {
    suspend fun saveSettings(settings: GameSettings): Result<Unit>
    suspend fun getSettings(): Result<GameSettings>
    suspend fun getSettingsByUserId(userId: String): Result<GameSettings>
    fun getSettingsFlow(): Flow<Result<GameSettings>>
    fun getSettingsFlowByUserId(userId: String): Flow<Result<GameSettings>>
    suspend fun updateTimerEnabled(isEnabled: Boolean): Result<Unit>
    suspend fun updateTimerEnabledByUserId(userId: String, isEnabled: Boolean): Result<Unit>
    suspend fun updateGameTimeLimit(timeLimit: Int): Result<Unit>
    suspend fun updateGameTimeLimitByUserId(userId: String, timeLimit: Int): Result<Unit>
    suspend fun updateLastPlayerName(playerName: String): Result<Unit>
    suspend fun updateLastPlayerNameByUserId(userId: String, playerName: String): Result<Unit>
}