package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

data class User(
    val uid: String,
    val displayName: String,
    val email: String,
    val createdAt: Long,
    val lastActiveAt: Long
)