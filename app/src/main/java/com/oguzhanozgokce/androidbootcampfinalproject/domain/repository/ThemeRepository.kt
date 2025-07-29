package com.oguzhanozgokce.androidbootcampfinalproject.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun setDarkTheme(isDark: Boolean)
    fun isDarkTheme(): Flow<Boolean>
}