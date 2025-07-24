package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

sealed class GameResult {
    data object InProgress : GameResult()
    data class Completed(val finalScore: Int, val timeUsed: Long) : GameResult()
    data object TimeUp : GameResult()
    data object Paused : GameResult()
}