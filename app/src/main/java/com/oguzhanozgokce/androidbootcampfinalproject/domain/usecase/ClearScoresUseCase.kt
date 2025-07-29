package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import javax.inject.Inject

class ClearScoresUseCase @Inject constructor(
    private val repository: GameScoreRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return repository.clearAllScoresByUserId(userId)
    }
}