package com.oguzhanozgokce.androidbootcampfinalproject.common

import com.oguzhanozgokce.androidbootcampfinalproject.common.exception.ExceptionMapper
import kotlinx.coroutines.withTimeout

/**
 * Simplified SafeCall - clean and easy to use.
 */
object SafeCall {

    /**
     * Executes a suspend function safely with timeout handling.
     *
     * @param timeoutMillis Timeout in milliseconds (default: 30 seconds)
     * @param operation The suspend function to execute
     * @return Result containing either success value or mapped exception
     */
    suspend fun <T> execute(
        timeoutMillis: Long = 30000L,
        operation: suspend () -> T
    ): Result<T> {
        return try {
            val result = withTimeout(timeoutMillis) { operation() }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(ExceptionMapper.mapException(e))
        }
    }

    /**
     * Executes a synchronous function safely.
     *
     * @param operation The function to execute
     * @return Result containing either success value or mapped exception
     */
    fun <T> executeSync(operation: () -> T): Result<T> {
        return try {
            Result.success(operation())
        } catch (e: Exception) {
            Result.failure(ExceptionMapper.mapException(e))
        }
    }
}

// Simple extension functions
suspend inline fun <T> safeCall(
    timeoutMillis: Long = 30000L,
    crossinline operation: suspend () -> T
): Result<T> = SafeCall.execute(timeoutMillis) { operation() }

inline fun <T> safeCallSync(
    crossinline operation: () -> T
): Result<T> = SafeCall.executeSync { operation() }
