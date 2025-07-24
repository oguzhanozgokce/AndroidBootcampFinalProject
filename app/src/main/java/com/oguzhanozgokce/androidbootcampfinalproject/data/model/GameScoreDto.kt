package com.oguzhanozgokce.androidbootcampfinalproject.data.model

data class GameScoreDto(
    val id: String? = null,
    val userId: String? = null,
    val playerName: String? = null,
    val score: Int? = null,
    val difficulty: String? = null,
    val gameTime: Long? = null,
    val completedTime: Long? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean? = null
) {
    // Firebase Firestore requires a no-argument constructor
    constructor() : this(null, null, null, null, null, null, null, System.currentTimeMillis(), null)
}