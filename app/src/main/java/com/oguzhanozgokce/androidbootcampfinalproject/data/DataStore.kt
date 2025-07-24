package com.oguzhanozgokce.androidbootcampfinalproject.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Professional DataStore implementation for user data management
 * Handles user authentication state and user information persistence
 */
@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "user_preferences"
        )

        // User Data Keys
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_DISPLAY_NAME_KEY = stringPreferencesKey("user_display_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_CREATED_AT_KEY = longPreferencesKey("user_created_at")
        private val USER_LAST_ACTIVE_AT_KEY = longPreferencesKey("user_last_active_at")

        // Auth State Keys
        private val IS_LOGGED_IN_KEY = stringPreferencesKey("is_logged_in")
        private val REMEMBER_ME_KEY = stringPreferencesKey("remember_me")
        private val LAST_LOGIN_TIME_KEY = longPreferencesKey("last_login_time")
    }

    /**
     * Save complete user information after successful login
     */
    suspend fun saveUserData(user: User, rememberMe: Boolean = false) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.uid
            preferences[USER_DISPLAY_NAME_KEY] = user.displayName
            preferences[USER_EMAIL_KEY] = user.email
            preferences[USER_CREATED_AT_KEY] = user.createdAt
            preferences[USER_LAST_ACTIVE_AT_KEY] = user.lastActiveAt
            preferences[IS_LOGGED_IN_KEY] = "true"
            preferences[REMEMBER_ME_KEY] = rememberMe.toString()
            preferences[LAST_LOGIN_TIME_KEY] = System.currentTimeMillis()
        }
    }

    /**
     * Get current user data as Flow
     */
    fun getCurrentUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY]
            val displayName = preferences[USER_DISPLAY_NAME_KEY]
            val email = preferences[USER_EMAIL_KEY]
            val createdAt = preferences[USER_CREATED_AT_KEY]
            val lastActiveAt = preferences[USER_LAST_ACTIVE_AT_KEY]

            if (userId != null && displayName != null && email != null &&
                createdAt != null && lastActiveAt != null
            ) {
                User(
                    uid = userId,
                    displayName = displayName,
                    email = email,
                    createdAt = createdAt,
                    lastActiveAt = lastActiveAt
                )
            } else {
                null
            }
        }
    }

    /**
     * Get user ID only
     */
    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    /**
     * Get user email only
     */
    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL_KEY]
        }
    }

    /**
     * Get user display name only
     */
    fun getUserDisplayName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_DISPLAY_NAME_KEY]
        }
    }

    /**
     * Check if user is currently logged in
     */
    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN_KEY] == "true"
        }
    }

    /**
     * Check if remember me was selected
     */
    fun isRememberMeEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[REMEMBER_ME_KEY] == "true"
        }
    }

    /**
     * Get last login time
     */
    fun getLastLoginTime(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_LOGIN_TIME_KEY] ?: 0L
        }
    }

    /**
     * Update user's last active time
     */
    suspend fun updateLastActiveTime(timestamp: Long = System.currentTimeMillis()) {
        context.dataStore.edit { preferences ->
            preferences[USER_LAST_ACTIVE_AT_KEY] = timestamp
        }
    }

    /**
     * Update user display name
     */
    suspend fun updateDisplayName(newDisplayName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_DISPLAY_NAME_KEY] = newDisplayName
        }
    }

    /**
     * Update user email
     */
    suspend fun updateEmail(newEmail: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = newEmail
        }
    }

    /**
     * Set remember me preference
     */
    suspend fun setRememberMe(remember: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMEMBER_ME_KEY] = remember.toString()
        }
    }

    /**
     * Clear all user data (logout)
     */
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_DISPLAY_NAME_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_CREATED_AT_KEY)
            preferences.remove(USER_LAST_ACTIVE_AT_KEY)
            preferences.remove(IS_LOGGED_IN_KEY)
            preferences.remove(REMEMBER_ME_KEY)
            preferences.remove(LAST_LOGIN_TIME_KEY)
        }
    }

    /**
     * Clear only auth state but keep user data (soft logout)
     */
    suspend fun clearAuthState() {
        context.dataStore.edit { preferences ->
            preferences.remove(IS_LOGGED_IN_KEY)
            preferences.remove(LAST_LOGIN_TIME_KEY)
        }
    }

    /**
     * Check if user session is valid based on time
     * @param maxSessionDuration Maximum session duration in milliseconds (default: 7 days)
     */
    fun isSessionValid(maxSessionDuration: Long = 7 * 24 * 60 * 60 * 1000L): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            val isLoggedIn = preferences[IS_LOGGED_IN_KEY] == "true"
            val lastLoginTime = preferences[LAST_LOGIN_TIME_KEY] ?: 0L
            val currentTime = System.currentTimeMillis()
            val rememberMe = preferences[REMEMBER_ME_KEY] == "true"

            if (!isLoggedIn) return@map false

            // If remember me is enabled, extend session duration
            val sessionDuration = if (rememberMe) maxSessionDuration * 4 else maxSessionDuration

            (currentTime - lastLoginTime) < sessionDuration
        }
    }

    /**
     * Get comprehensive user session info
     */
    data class UserSession(
        val user: User?,
        val isLoggedIn: Boolean,
        val rememberMe: Boolean,
        val lastLoginTime: Long,
        val isSessionValid: Boolean
    )

    /**
     * Get complete user session information
     */
    fun getUserSession(): Flow<UserSession> {
        return context.dataStore.data.map { preferences ->
            val user = run {
                val userId = preferences[USER_ID_KEY]
                val displayName = preferences[USER_DISPLAY_NAME_KEY]
                val email = preferences[USER_EMAIL_KEY]
                val createdAt = preferences[USER_CREATED_AT_KEY]
                val lastActiveAt = preferences[USER_LAST_ACTIVE_AT_KEY]

                if (userId != null && displayName != null && email != null &&
                    createdAt != null && lastActiveAt != null
                ) {
                    User(
                        uid = userId,
                        displayName = displayName,
                        email = email,
                        createdAt = createdAt,
                        lastActiveAt = lastActiveAt
                    )
                } else {
                    null
                }
            }

            val isLoggedIn = preferences[IS_LOGGED_IN_KEY] == "true"
            val rememberMe = preferences[REMEMBER_ME_KEY] == "true"
            val lastLoginTime = preferences[LAST_LOGIN_TIME_KEY] ?: 0L
            val currentTime = System.currentTimeMillis()
            val sessionDuration = if (rememberMe) 28 * 24 * 60 * 60 * 1000L else 7 * 24 * 60 * 60 * 1000L
            val isSessionValid = isLoggedIn && (currentTime - lastLoginTime) < sessionDuration

            UserSession(
                user = user,
                isLoggedIn = isLoggedIn,
                rememberMe = rememberMe,
                lastLoginTime = lastLoginTime,
                isSessionValid = isSessionValid
            )
        }
    }
}