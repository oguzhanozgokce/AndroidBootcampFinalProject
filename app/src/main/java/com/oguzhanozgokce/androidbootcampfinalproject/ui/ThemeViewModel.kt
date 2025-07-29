package com.oguzhanozgokce.androidbootcampfinalproject.ui

import androidx.lifecycle.ViewModel
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.ThemeRepository
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetCurrentUserUseCase
import com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase.GetGameSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getGameSettingsUseCase: GetGameSettingsUseCase
) : ViewModel() {

    val isDarkTheme: Flow<Boolean> = themeRepository.isDarkTheme()
}