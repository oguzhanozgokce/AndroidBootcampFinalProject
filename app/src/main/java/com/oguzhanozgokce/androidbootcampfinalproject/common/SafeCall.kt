package com.oguzhanozgokce.androidbootcampfinalproject.common

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.net.UnknownHostException

object SafeCall {

    suspend fun <T> execute(
        timeoutMillis: Long = 30000L,
        operation: suspend () -> T
    ): Resource<T> {
        return try {
            val result = withTimeout(timeoutMillis) {
                operation()
            }
            Resource.Success(result)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    fun <T> executeSync(operation: () -> T): Resource<T> {
        return try {
            val result = operation()
            Resource.Success(result)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun <T> handleException(exception: Exception): Resource.Error<T> {
        return when (exception) {
            // Firebase Specific Errors
            is FirebaseFirestoreException -> {
                val errorCode = mapFirestoreErrorCode(exception.code)
                Resource.Error(
                    message = FirebaseErrorCode.getMessageByCode(errorCode.code),
                    errorCode = errorCode.code
                )
            }

            is FirebaseNetworkException -> {
                Resource.Error(
                    message = FirebaseErrorCode.NETWORK_ERROR.message,
                    errorCode = FirebaseErrorCode.NETWORK_ERROR.code
                )
            }

            is FirebaseTooManyRequestsException -> {
                Resource.Error(
                    message = FirebaseErrorCode.QUOTA_EXCEEDED.message,
                    errorCode = FirebaseErrorCode.QUOTA_EXCEEDED.code
                )
            }

            is FirebaseException -> {
                Resource.Error(
                    message = FirebaseErrorCode.UNKNOWN.message,
                    errorCode = FirebaseErrorCode.UNKNOWN.code
                )
            }

            // Firebase Auth Errors
            is FirebaseAuthException -> {
                val errorCode = mapFirebaseAuthErrorCode(exception.errorCode)
                Resource.Error(
                    message = FirebaseErrorCode.getMessageByCode(errorCode.code),
                    errorCode = errorCode.code
                )
            }

            // Network Errors
            is UnknownHostException, is IOException -> {
                Resource.Error(
                    message = FirebaseErrorCode.NETWORK_ERROR.message,
                    errorCode = FirebaseErrorCode.NETWORK_ERROR.code
                )
            }

            // Timeout Errors
            is TimeoutCancellationException -> {
                Resource.Error(
                    message = FirebaseErrorCode.TIMEOUT.message,
                    errorCode = FirebaseErrorCode.TIMEOUT.code
                )
            }

            // Generic Errors
            else -> {
                Resource.Error(
                    message = exception.message ?: FirebaseErrorCode.UNKNOWN.message,
                    errorCode = FirebaseErrorCode.UNKNOWN.code
                )
            }
        }
    }

    private fun mapFirestoreErrorCode(code: FirebaseFirestoreException.Code): FirebaseErrorCode {
        // Firestore returns enum Code, more type-safe approach
        return when (code) {
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirebaseErrorCode.PERMISSION_DENIED
            FirebaseFirestoreException.Code.NOT_FOUND -> FirebaseErrorCode.NOT_FOUND
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> FirebaseErrorCode.ALREADY_EXISTS
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> FirebaseErrorCode.FAILED_PRECONDITION
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> FirebaseErrorCode.OUT_OF_RANGE
            FirebaseFirestoreException.Code.DATA_LOSS -> FirebaseErrorCode.DATA_LOSS
            FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> FirebaseErrorCode.RESOURCE_EXHAUSTED
            FirebaseFirestoreException.Code.INTERNAL -> FirebaseErrorCode.INTERNAL
            FirebaseFirestoreException.Code.INVALID_ARGUMENT -> FirebaseErrorCode.INVALID_ARGUMENT
            FirebaseFirestoreException.Code.CANCELLED -> FirebaseErrorCode.CANCELLED
            FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> FirebaseErrorCode.DEADLINE_EXCEEDED
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> FirebaseErrorCode.UNAUTHENTICATED
            FirebaseFirestoreException.Code.UNAVAILABLE -> FirebaseErrorCode.UNAVAILABLE
            else -> FirebaseErrorCode.UNKNOWN
        }
    }

    private fun mapFirebaseAuthErrorCode(code: String): FirebaseErrorCode {
        // Firebase Auth returns String errorCode, less type-safe but more flexible
        return when (code) {
            "ERROR_INVALID_EMAIL" -> FirebaseErrorCode.AUTH_INVALID_EMAIL
            "ERROR_WRONG_PASSWORD" -> FirebaseErrorCode.AUTH_WRONG_PASSWORD
            "ERROR_EMAIL_ALREADY_IN_USE" -> FirebaseErrorCode.AUTH_EMAIL_ALREADY_IN_USE
            "ERROR_USER_NOT_FOUND" -> FirebaseErrorCode.AUTH_USER_NOT_FOUND
            "ERROR_USER_DISABLED" -> FirebaseErrorCode.AUTH_USER_DISABLED
            "ERROR_TOO_MANY_REQUESTS" -> FirebaseErrorCode.AUTH_TOO_MANY_REQUESTS
            "ERROR_OPERATION_NOT_ALLOWED" -> FirebaseErrorCode.AUTH_OPERATION_NOT_ALLOWED
            "ERROR_WEAK_PASSWORD" -> FirebaseErrorCode.AUTH_WEAK_PASSWORD
            "ERROR_INVALID_CREDENTIAL" -> FirebaseErrorCode.AUTH_INVALID_CREDENTIAL
            "ERROR_USER_TOKEN_EXPIRED" -> FirebaseErrorCode.AUTH_USER_TOKEN_EXPIRED
            "ERROR_INVALID_USER_TOKEN" -> FirebaseErrorCode.AUTH_INVALID_USER_TOKEN
            "ERROR_REQUIRES_RECENT_LOGIN" -> FirebaseErrorCode.AUTH_REQUIRES_RECENT_LOGIN
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> FirebaseErrorCode.AUTH_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL
            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> FirebaseErrorCode.AUTH_CREDENTIAL_ALREADY_IN_USE
            "ERROR_INVALID_VERIFICATION_CODE" -> FirebaseErrorCode.AUTH_INVALID_VERIFICATION_CODE
            "ERROR_INVALID_VERIFICATION_ID" -> FirebaseErrorCode.AUTH_INVALID_VERIFICATION_ID
            "ERROR_EXPIRED_ACTION_CODE" -> FirebaseErrorCode.AUTH_EXPIRED_ACTION_CODE
            "ERROR_INVALID_ACTION_CODE" -> FirebaseErrorCode.AUTH_INVALID_ACTION_CODE
            // Legacy codes without ERROR_ prefix (for compatibility)
            "INVALID_EMAIL" -> FirebaseErrorCode.AUTH_INVALID_EMAIL
            "WRONG_PASSWORD" -> FirebaseErrorCode.AUTH_WRONG_PASSWORD
            "EMAIL_ALREADY_IN_USE" -> FirebaseErrorCode.AUTH_EMAIL_ALREADY_IN_USE
            "USER_NOT_FOUND" -> FirebaseErrorCode.AUTH_USER_NOT_FOUND
            "USER_DISABLED" -> FirebaseErrorCode.AUTH_USER_DISABLED
            "TOO_MANY_REQUESTS" -> FirebaseErrorCode.AUTH_TOO_MANY_REQUESTS
            "OPERATION_NOT_ALLOWED" -> FirebaseErrorCode.AUTH_OPERATION_NOT_ALLOWED
            "WEAK_PASSWORD" -> FirebaseErrorCode.AUTH_WEAK_PASSWORD
            else -> FirebaseErrorCode.UNKNOWN
        }
    }
}

// Extension functions for easier usage
suspend inline fun <T> safeCall(
    timeoutMillis: Long = 30000L,
    crossinline operation: suspend () -> T
): Resource<T> = SafeCall.execute(timeoutMillis) { operation() }

inline fun <T> safeCallSync(
    crossinline operation: () -> T
): Resource<T> = SafeCall.executeSync { operation() }