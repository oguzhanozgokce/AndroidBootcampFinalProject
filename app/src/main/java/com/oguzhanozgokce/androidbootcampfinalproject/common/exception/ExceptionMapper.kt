package com.oguzhanozgokce.androidbootcampfinalproject.common.exception

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException
import java.net.UnknownHostException

/**
 * Exception mapper that converts any exception to BaseException or its subclasses.
 */
object ExceptionMapper {

    fun mapException(exception: Throwable): BaseException {
        return when (exception) {
            // Already our exception - pass through
            is BaseException -> exception

            // Firebase Auth Exceptions
            is FirebaseAuthException -> when (exception.errorCode) {
                "ERROR_USER_NOT_FOUND" -> AuthorizationException("User not found")
                "ERROR_WRONG_PASSWORD" -> AuthorizationException("Invalid credentials")
                "ERROR_INVALID_EMAIL" -> BadRequestException("Invalid email format")
                "ERROR_EMAIL_ALREADY_IN_USE" -> BadRequestException("Email already in use")
                "ERROR_WEAK_PASSWORD" -> BadRequestException("Password is too weak")
                else -> AuthorizationException(exception.message ?: "Authentication failed")
            }

            // Firebase Firestore Exceptions
            is FirebaseFirestoreException -> when (exception.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AuthorizationException("Access denied")
                FirebaseFirestoreException.Code.NOT_FOUND -> NotFoundException("Document not found")
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> AuthorizationException("Please login again")
                FirebaseFirestoreException.Code.UNAVAILABLE -> NetworkException("Service unavailable")
                FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> NetworkException("Request timeout")
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> BadRequestException("Invalid request")
                else -> UnknownException(exception.message ?: "Database error occurred")
            }

            // Network Related Exceptions
            is FirebaseNetworkException,
            is UnknownHostException,
            is IOException -> NetworkException(exception.message ?: "Network error")

            is FirebaseTooManyRequestsException -> BadRequestException("Too many requests, please try again later")

            // Timeout Exceptions
            is TimeoutCancellationException -> NetworkException("Request timeout")

            // Generic Exceptions
            else -> UnknownException(exception.message ?: "An unknown error occurred")
        }
    }
}
