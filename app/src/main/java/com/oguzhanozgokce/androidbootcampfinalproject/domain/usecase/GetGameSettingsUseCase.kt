package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameSettingsUseCase @Inject constructor(
    private val repository: GameSettingsRepository
) {
    suspend operator fun invoke(userId: String): Result<GameSettings> {
        return repository.getSettingsByUserId(userId)
    }

    fun flow(userId: String): Flow<Result<GameSettings>> {
        return repository.getSettingsFlowByUserId(userId)
    }
}