package com.oguzhanozgokce.androidbootcampfinalproject.domain.model

data class GameCard(
    val id: String,
    val number: Int,
    val isFlipped: Boolean,
    val isMatched: Boolean,
    val position: Int
)