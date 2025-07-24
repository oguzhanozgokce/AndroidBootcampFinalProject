package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import kotlinx.coroutines.flow.Flow

interface GameSettingsRepository {
    suspend fun saveSettings(settings: GameSettings): Resource<Unit>
    suspend fun getSettings(): Resource<GameSettings>
    suspend fun getSettingsByUserId(userId: String): Resource<GameSettings>
    fun getSettingsFlow(): Flow<Resource<GameSettings>>
    fun getSettingsFlowByUserId(userId: String): Flow<Resource<GameSettings>>
    suspend fun updateTimerEnabled(isEnabled: Boolean): Resource<Unit>
    suspend fun updateTimerEnabledByUserId(userId: String, isEnabled: Boolean): Resource<Unit>
    suspend fun updateGameTimeLimit(timeLimit: Int): Resource<Unit>
    suspend fun updateGameTimeLimitByUserId(userId: String, timeLimit: Int): Resource<Unit>
    suspend fun updateLastPlayerName(playerName: String): Resource<Unit>
    suspend fun updateLastPlayerNameByUserId(userId: String, playerName: String): Resource<Unit>
}