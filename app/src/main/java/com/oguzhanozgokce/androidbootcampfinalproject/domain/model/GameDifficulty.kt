package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

enum class GameDifficulty(val cardCount: Int, val uniqueNumbers: Int) {
    EASY(12, 8),
    MEDIUM(16, 10),
    HARD(20, 12)
}