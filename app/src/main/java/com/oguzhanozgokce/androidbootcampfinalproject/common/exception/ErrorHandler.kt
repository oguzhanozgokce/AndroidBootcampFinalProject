package com.oguzhanozgokce.androidbootcampfinalproject.common.exception

import android.util.Log

/**
 * Centralized error handling and logging system.
 * This handles logging, analytics, and user notification for exceptions.
 */
object ErrorHandler {

    private const val TAG = "ErrorHandler"

    /**
     * Handles an exception by logging it and returning a user-friendly message.
     */
    fun handleException(exception: BaseException): String {
        // Log the exception
        when (exception) {
            is NetworkException -> Log.w(TAG, "Network error: ${exception.message}", exception)
            is AuthorizationException -> Log.w(TAG, "Authorization error: ${exception.message}", exception)
            is BadRequestException -> Log.d(TAG, "Bad request: ${exception.message}", exception)
            is NotFoundException -> Log.d(TAG, "Not found: ${exception.message}", exception)
            is UnknownException -> Log.e(TAG, "Unknown error: ${exception.message}", exception)
            else -> Log.e(TAG, "Unhandled error: ${exception.message}", exception)
        }

        return exception.message ?: "An error occurred"
    }

    /**
     * Handles a generic throwable by mapping it to BaseException first.
     */
    fun handleException(throwable: Throwable): String {
        val mappedException = ExceptionMapper.mapException(throwable)
        return handleException(mappedException)
    }
}
