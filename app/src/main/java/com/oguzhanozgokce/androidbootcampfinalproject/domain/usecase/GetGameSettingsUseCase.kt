package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.GameSettings
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameSettingsRepository
import javax.inject.Inject

class GetGameSettingsUseCase @Inject constructor(
    private val repository: GameSettingsRepository
) {
    suspend operator fun invoke(): Resource<GameSettings> {
        return repository.getSettings()
    }
}