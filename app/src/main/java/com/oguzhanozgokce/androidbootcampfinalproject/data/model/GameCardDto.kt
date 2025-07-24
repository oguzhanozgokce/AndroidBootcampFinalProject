package com.oguzhanozgokce.androidbootcampfinalproject.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GameCardDto(
    val id: String? = null,
    val number: Int? = null,
    val isFlipped: Boolean? = null,
    val isMatched: Boolean? = null,
    val position: Int? = null
)