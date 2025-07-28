package com.oguzhanozgokce.androidbootcampfinalproject.data.mapper

import com.oguzhanozgokce.androidbootcampfinalproject.common.orEmpty
import com.oguzhanozgokce.androidbootcampfinalproject.common.orFalse
import com.oguzhanozgokce.androidbootcampfinalproject.common.orTrue
import com.oguzhanozgokce.androidbootcampfinalproject.common.orZero
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.GameCardDto
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.GameScoreDto
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.GameSettingsDto
import com.oguzhanozgokce.androidbootcampfinalproject.data.model.UserDto
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameCard
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameDifficulty
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User

fun GameScoreDto.toDomain(): GameScore {
    return GameScore(
        id = id.orEmpty(),
        userId = userId.orEmpty(),
        playerName = playerName.orEmpty(),
        score = score.orZero(),
        difficulty = when (difficulty.orEmpty()) {
            "EASY" -> GameDifficulty.EASY
            "MEDIUM" -> GameDifficulty.MEDIUM
            "HARD" -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        },
        gameTime = gameTime.orZero(),
        completedTime = completedTime.orZero(),
        timestamp = timestamp,
        completed = completed.orFalse()
    )
}

fun GameScore.toDto(): GameScoreDto {
    return GameScoreDto(
        id = id,
        userId = userId,
        playerName = playerName,
        score = score,
        difficulty = difficulty.name,
        gameTime = gameTime,
        completedTime = completedTime,
        timestamp = timestamp,
        completed = completed
    )
}

fun List<GameScoreDto>.toDomainList(): List<GameScore> {
    return map { it.toDomain() }
}

fun List<GameScore>.toDtoList(): List<GameScoreDto> {
    return map { it.toDto() }
}

// GameSettings Mappers
fun GameSettingsDto.toDomain(): GameSettings {
    return GameSettings(
        userId = userId.orEmpty(),
        isDarkTheme = isDarkTheme.orFalse(),
        isTimerEnabled = isTimerEnabled.orTrue(),
        gameTimeLimit = gameTimeLimit ?: 60,
        lastPlayerName = lastPlayerName.orEmpty()
    )
}

fun GameSettings.toDto(): GameSettingsDto {
    return GameSettingsDto(
        userId = userId,
        isDarkTheme = isDarkTheme,
        isTimerEnabled = isTimerEnabled,
        gameTimeLimit = gameTimeLimit,
        lastPlayerName = lastPlayerName
    )
}

// GameCard Mappers
fun GameCardDto.toDomain(): GameCard {
    return GameCard(
        id = id.orEmpty(),
        number = number.orZero(),
        isFlipped = flipped.orFalse(),
        isMatched = matched.orFalse(),
        position = position.orZero()
    )
}

fun GameCard.toDto(): GameCardDto {
    return GameCardDto(
        id = id,
        number = number,
        flipped = isFlipped,
        matched = isMatched,
        position = position
    )
}

fun List<GameCardDto>.toCardDomainList(): List<GameCard> {
    return map { it.toDomain() }
}

fun List<GameCard>.toCardDtoList(): List<GameCardDto> {
    return map { it.toDto() }
}

// User Mappers
fun UserDto.toDomain(): User {
    return User(
        uid = uid.orEmpty(),
        displayName = displayName.orEmpty(),
        email = email.orEmpty(),
        createdAt = createdAt,
        lastActiveAt = lastActiveAt
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        uid = uid,
        displayName = displayName,
        email = email,
        createdAt = createdAt,
        lastActiveAt = lastActiveAt
    )
}

// Game Creation Helper
fun createGameCards(difficulty: GameDifficulty): List<GameCard> {
    val numbers = (1..100).shuffled().take(difficulty.uniqueNumbers)
    val doubledNumbers = (numbers + numbers).shuffled()

    return doubledNumbers.mapIndexed { index, number ->
        GameCard(
            id = "card_${index}_${System.currentTimeMillis()}",
            number = number,
            isFlipped = false,
            isMatched = false,
            position = index
        )
    }
}

// Score Calculation Helper
fun calculateScore(
    matchedPairs: Int,
    timeUsed: Long,
    difficulty: GameDifficulty,
    totalTime: Int
): Int {
    val baseScore = matchedPairs * 100
    val timeBonus = ((totalTime - timeUsed) * 2).coerceAtLeast(0)
    val difficultyMultiplier = when (difficulty) {
        GameDifficulty.EASY -> 1.0
        GameDifficulty.MEDIUM -> 1.2
        GameDifficulty.HARD -> 1.5
    }

    return ((baseScore + timeBonus) * difficultyMultiplier).toInt()
}