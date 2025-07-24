package com.oguzhanozgokce.androidbootcampfinalproject.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis()
)