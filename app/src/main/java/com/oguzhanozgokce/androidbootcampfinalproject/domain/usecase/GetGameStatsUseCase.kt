package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.data.UserDataStore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameScore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetGameStatsUseCase @Inject constructor(
    private val gameScoreRepository: GameScoreRepository,
    private val userDataStore: UserDataStore
) {

    suspend operator fun invoke(): Result<List<GameScore>> {
        val userId = userDataStore.getUserId().first()

        return if (userId != null) {
            gameScoreRepository.getAllScoresByUserId(userId)
        } else {
            Result.success(emptyList())
        }
    }
}
