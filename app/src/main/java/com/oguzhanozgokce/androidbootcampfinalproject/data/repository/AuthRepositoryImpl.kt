package com.oguzhanozgokce.androidbootcampfinalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzhanozgokce.androidbootcampfinalproject.common.safeCall
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDomain
import com.oguzhanozgokce.androidbootcampfinalproject.data.mapper.toDto
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.UserDto
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    companion object {
        private const val COLLECTION_USERS = "users"
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> = safeCall {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Sign in failed")

        // Get user from Firestore
        val userDoc = firestore.collection(COLLECTION_USERS)
            .document(firebaseUser.uid)
            .get()
            .await()

        if (userDoc.exists()) {
            userDoc.toObject(UserDto::class.java)?.toDomain()
                ?: throw Exception("User data not found")
        } else {
            // Create user data if not exists
            val user = User(
                uid = firebaseUser.uid,
                displayName = firebaseUser.displayName ?: "User",
                email = firebaseUser.email ?: email,
                createdAt = System.currentTimeMillis(),
                lastActiveAt = System.currentTimeMillis()
            )

            firestore.collection(COLLECTION_USERS)
                .document(user.uid)
                .set(user.toDto())
                .await()

            user
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        displayName: String
    ): Result<User> = safeCall {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Sign up failed")

        // Update display name in Firebase Auth
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        firebaseUser.updateProfile(profileUpdates).await()

        // Create user in Firestore
        val user = User(
            uid = firebaseUser.uid,
            displayName = displayName,
            email = email,
            createdAt = System.currentTimeMillis(),
            lastActiveAt = System.currentTimeMillis()
        )

        firestore.collection(COLLECTION_USERS)
            .document(user.uid)
            .set(user.toDto())
            .await()

        user
    }

    override suspend fun getCurrentUser(): Result<User?> = safeCall {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val userDoc = firestore.collection(COLLECTION_USERS)
                .document(firebaseUser.uid)
                .get()
                .await()

            if (userDoc.exists()) {
                userDoc.toObject(UserDto::class.java)?.toDomain()
            } else {
                val user = User(
                    uid = firebaseUser.uid,
                    displayName = firebaseUser.displayName ?: "User",
                    email = firebaseUser.email ?: "",
                    createdAt = System.currentTimeMillis(),
                    lastActiveAt = System.currentTimeMillis()
                )
                firestore.collection(COLLECTION_USERS)
                    .document(user.uid)
                    .set(user.toDto())
                    .await()
                user
            }
        } else {
            null
        }
    }

    override fun getCurrentUserFlow(): Flow<Result<User?>> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                // Listen to user document changes
                val userDocListener = firestore.collection(COLLECTION_USERS)
                    .document(firebaseUser.uid)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(Result.failure(Exception(error.message ?: "Unknown error")))
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {
                            val user = snapshot.toObject(UserDto::class.java)?.toDomain()
                            trySend(Result.success(user))
                        } else {
                            trySend(Result.success(null))
                        }
                    }
            } else {
                trySend(Result.success(null))
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun updateDisplayName(displayName: String): Result<Unit> = safeCall {
        val currentUser = firebaseAuth.currentUser
            ?: throw Exception("User not authenticated")

        // Update in Firebase Auth first
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        currentUser.updateProfile(profileUpdates).await()

        // Update in Firestore
        firestore.collection(COLLECTION_USERS)
            .document(currentUser.uid)
            .update(
                "displayName",
                displayName,
                "lastActiveAt",
                System.currentTimeMillis()
            )
            .await()
    }

    override suspend fun signOut(): Result<Unit> = safeCall {
        firebaseAuth.signOut()
    }

    override fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}