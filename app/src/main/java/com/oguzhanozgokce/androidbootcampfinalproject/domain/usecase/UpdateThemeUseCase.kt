package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameSettingsRepository
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.ThemeRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val gameSettingsRepository: GameSettingsRepository,
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(userId: String, isDarkTheme: Boolean): Result<Unit> {
        return try {
            themeRepository.setDarkTheme(isDarkTheme)
            gameSettingsRepository.updateDarkThemeByUserId(userId, isDarkTheme)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}