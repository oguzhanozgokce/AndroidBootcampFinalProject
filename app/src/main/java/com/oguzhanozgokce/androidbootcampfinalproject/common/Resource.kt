package com.oguzhanozgokce.androidbootcampfinalproject.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val errorCode: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(
        message: String,
        data: T? = null,
        errorCode: String? = null
    ) : Resource<T>(data, message, errorCode)
}