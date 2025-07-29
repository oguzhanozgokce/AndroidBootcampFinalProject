package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameSettingsRepository
import javax.inject.Inject

class UpdateTimerUseCase @Inject constructor(
    private val repository: GameSettingsRepository
) {
    suspend operator fun invoke(userId: String, isTimerEnabled: Boolean): Result<Unit> {
        return repository.updateTimerEnabledByUserId(userId, isTimerEnabled)
    }
}