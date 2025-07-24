package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<User>
    suspend fun signUpWithEmailAndPassword(email: String, password: String, displayName: String): Resource<User>
    suspend fun getCurrentUser(): Resource<User?>
    fun getCurrentUserFlow(): Flow<Resource<User?>>
    suspend fun updateDisplayName(displayName: String): Resource<Unit>
    suspend fun signOut(): Resource<Unit>
    fun isUserSignedIn(): Boolean
}