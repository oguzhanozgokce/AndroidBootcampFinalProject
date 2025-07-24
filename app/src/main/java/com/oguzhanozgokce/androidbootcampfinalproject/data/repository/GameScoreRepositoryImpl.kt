package com.oguzhanozgokce.androidbootcampfinalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
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

    override suspend fun saveScore(gameScore: GameScore): Resource<String> = safeCall {
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

    override suspend fun getAllScores(): Resource<List<GameScore>> = safeCall {
        val currentUser = firestore.collection("users").get().await() // Bu geçici, gerçekte auth'dan alınacak
        throw Exception("getCurrentUserId() method needed - implement with AuthRepository")
    }

    override suspend fun getAllScoresByUserId(userId: String): Resource<List<GameScore>> = safeCall {
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

    override suspend fun getTopScores(limit: Int): Resource<List<GameScore>> = safeCall {
        throw Exception("getTopScoresByUserId() method should be used instead")
    }

    override suspend fun getTopScoresByUserId(userId: String, limit: Int): Resource<List<GameScore>> = safeCall {
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

    override suspend fun deleteScore(scoreId: String): Resource<Unit> = safeCall {
        firestore.collection(COLLECTION_SCORES)
            .document(scoreId)
            .delete()
            .await()
    }

    override suspend fun clearAllScores(): Resource<Unit> = safeCall {
        throw Exception("clearAllScoresByUserId() method should be used instead")
    }

    override suspend fun clearAllScoresByUserId(userId: String): Resource<Unit> = safeCall {
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

    override fun getScoresFlow(): Flow<Resource<List<GameScore>>> = callbackFlow {
        throw Exception("getScoresFlowByUserId() method should be used instead")
    }

    override fun getScoresFlowByUserId(userId: String): Flow<Resource<List<GameScore>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_SCORES)
            .whereEqualTo("userId", userId)
            .orderBy("score", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Unknown error"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val scores = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(GameScoreDto::class.java)?.copy(id = doc.id)
                    }.toDomainList()
                    trySend(Resource.Success(scores))
                }
            }

        awaitClose { listener.remove() }
    }
}