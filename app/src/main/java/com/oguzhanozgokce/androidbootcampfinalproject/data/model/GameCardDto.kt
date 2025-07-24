package com.oguzhanozgokce.androidbootcampfinalproject.data.model

data class GameCardDto(
    val id: String? = null,
    val number: Int? = null,
    val flipped: Boolean? = null,
    val matched: Boolean? = null,
    val position: Int? = null
) {
    // Firebase Firestore requires a no-argument constructor
    constructor() : this(null, null, null, null, null)
}
