package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun signUpWithEmailAndPassword(email: String, password: String, displayName: String): Result<User>
    suspend fun getCurrentUser(): Result<User?>
    fun getCurrentUserFlow(): Flow<Result<User?>>
    suspend fun updateDisplayName(displayName: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun isUserSignedIn(): Boolean
}