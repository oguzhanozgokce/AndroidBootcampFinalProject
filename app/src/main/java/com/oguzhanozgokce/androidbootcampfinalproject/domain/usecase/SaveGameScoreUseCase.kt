package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import javax.inject.Inject

class SaveGameScoreUseCase @Inject constructor(
    private val repository: GameScoreRepository
) {
    suspend operator fun invoke(gameScore: GameScore): Result<String> {
        return repository.saveScore(gameScore)
    }
}