package com.oguzhanozgokce.androidbootcampfinalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.oguzhanozgokce.androidbootcampfinalproject.common.safeCall
import com.oguzhanozgokce.androidbootcampfinalproject.common.exception.ErrorHandler
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDomain
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDto
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.GameSettingsDto
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameSettingsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameSettingsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GameSettingsRepository {

    companion object {
        private const val COLLECTION_SETTINGS = "game_settings"
    }

    override suspend fun saveSettings(settings: GameSettings): Result<Unit> = safeCall {
        firestore.collection(COLLECTION_SETTINGS)
            .document(settings.userId)
            .set(settings.toDto())
            .await()
    }

    override suspend fun getSettings(): Result<GameSettings> = safeCall {
        throw Exception("getSettingsByUserId() method should be used instead")
    }

    override suspend fun getSettingsByUserId(userId: String): Result<GameSettings> = safeCall {
        val snapshot = firestore.collection(COLLECTION_SETTINGS)
            .document(userId)
            .get()
            .await()

        if (snapshot.exists()) {
            snapshot.toObject(GameSettingsDto::class.java)?.toDomain()
                ?: getDefaultSettings(userId)
        } else {
            getDefaultSettings(userId)
        }
    }

    override fun getSettingsFlow(): Flow<Result<GameSettings>> = callbackFlow {
        throw Exception("getSettingsFlowByUserId() method should be used instead")
    }

    override fun getSettingsFlowByUserId(userId: String): Flow<Result<GameSettings>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_SETTINGS)
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    val errorMessage = ErrorHandler.handleException(error)
                    trySend(Result.failure(Exception(errorMessage)))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val settings = snapshot.toObject(GameSettingsDto::class.java)?.toDomain()
                        ?: getDefaultSettings(userId)
                    trySend(Result.success(settings))
                } else {
                    trySend(Result.success(getDefaultSettings(userId)))
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun updateTimerEnabled(isEnabled: Boolean): Result<Unit> = safeCall {
        throw Exception("updateTimerEnabledByUserId() method should be used instead")
    }

    override suspend fun updateTimerEnabledByUserId(userId: String, isEnabled: Boolean): Result<Unit> = safeCall {
        firestore.collection(COLLECTION_SETTINGS)
            .document(userId)
            .update("isTimerEnabled", isEnabled)
            .await()
    }

    override suspend fun updateGameTimeLimit(timeLimit: Int): Result<Unit> = safeCall {
        throw Exception("updateGameTimeLimitByUserId() method should be used instead")
    }

    override suspend fun updateGameTimeLimitByUserId(userId: String, timeLimit: Int): Result<Unit> = safeCall {
        firestore.collection(COLLECTION_SETTINGS)
            .document(userId)
            .update("gameTimeLimit", timeLimit)
            .await()
    }

    override suspend fun updateLastPlayerName(playerName: String): Result<Unit> = safeCall {
        throw Exception("updateLastPlayerNameByUserId() method should be used instead")
    }

    override suspend fun updateLastPlayerNameByUserId(userId: String, playerName: String): Result<Unit> = safeCall {
        firestore.collection(COLLECTION_SETTINGS)
            .document(userId)
            .update("lastPlayerName", playerName)
            .await()
    }

    private fun getDefaultSettings(userId: String): GameSettings {
        return GameSettings(
            userId = userId,
            isDarkTheme = false,
            isTimerEnabled = true,
            gameTimeLimit = 60,
            lastPlayerName = ""
        )
    }
}