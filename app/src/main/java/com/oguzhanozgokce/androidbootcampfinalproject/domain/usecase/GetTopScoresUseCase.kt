package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import javax.inject.Inject

class GetTopScoresUseCase @Inject constructor(
    private val repository: GameScoreRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<GameScore>> {
        return repository.getTopScores(limit)
    }
}