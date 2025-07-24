package com.oguzhanozgokce.androidbootcampfinalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.oguzhanozgokce.androidbootcampfinalproject.common.exception.ErrorHandler
import com.oguzhanozgokce.androidbootcampfinalproject.common.safeCall
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDomain
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDomainList
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDto
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.GameScoreDto
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameScoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GameScoreRepository {

    companion object {
        private const val COLLECTION_SCORES = "game_scores"
    }

    override suspend fun saveScore(gameScore: GameScore): Result<String> = safeCall {
        val scoreDto = gameScore.toDto()
        val docRef = if (scoreDto.id.isNullOrEmpty()) {
            firestore.collection(COLLECTION_SCORES).document()
        } else {
            firestore.collection(COLLECTION_SCORES).document(scoreDto.id!!)
        }

        val scoreWithId = scoreDto.copy(id = docRef.id)
        docRef.set(scoreWithId).await()
        docRef.id
    }

    override suspend fun getAllScores(): Result<List<GameScore>> = safeCall {
        throw Exception("getCurrentUserId() method needed - implement with AuthRepository")
    }

    override suspend fun getAllScoresByUserId(userId: String): Result<List<GameScore>> = safeCall {
        val snapshot = firestore.collection(COLLECTION_SCORES)
            .whereEqualTo("userId", userId)
            .orderBy("score", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(GameScoreDto::class.java)?.copy(id = doc.id)
        }.toDomainList()
    }

    override suspend fun getTopScores(limit: Int): Result<List<GameScore>> = safeCall {
        throw Exception("getTopScoresByUserId() method should be used instead")
    }

    override suspend fun getTopScoresByUserId(userId: String, limit: Int): Result<List<GameScore>> = safeCall {
        val snapshot = firestore.collection(COLLECTION_SCORES)
            .whereEqualTo("userId", userId)
            .orderBy("score", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()

        snapshot.documents.mapNotNull { doc ->
            doc.toObject(GameScoreDto::class.java)?.copy(id = doc.id)
        }.toDomainList()
    }

    override suspend fun deleteScore(scoreId: String): Result<Unit> = safeCall {
        firestore.collection(COLLECTION_SCORES)
            .document(scoreId)
            .delete()
            .await()
    }

    override suspend fun clearAllScores(): Result<Unit> = safeCall {
        throw Exception("clearAllScoresByUserId() method should be used instead")
    }

    override suspend fun clearAllScoresByUserId(userId: String): Result<Unit> = safeCall {
        val snapshot = firestore.collection(COLLECTION_SCORES)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val batch = firestore.batch()
        snapshot.documents.forEach { doc ->
            batch.delete(doc.reference)
        }
        batch.commit().await()
    }

    override fun getScoresFlow(): Flow<Result<List<GameScore>>> = callbackFlow {
        throw Exception("getScoresFlowByUserId() method should be used instead")
    }

    override fun getScoresFlowByUserId(userId: String): Flow<Result<List<GameScore>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_SCORES)
            .whereEqualTo("userId", userId)
            .orderBy("score", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    val errorMessage = ErrorHandler.handleException(error)
                    trySend(Result.failure(Exception(errorMessage)))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val scores = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(GameScoreDto::class.java)?.copy(id = doc.id)
                    }.toDomainList()
                    trySend(Result.success(scores))
                }
            }

        awaitClose { listener.remove() }
    }
}