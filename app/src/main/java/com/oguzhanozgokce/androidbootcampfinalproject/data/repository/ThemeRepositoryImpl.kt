package com.oguzhanozgokce.androidbootcampfinalproject.data.repository

import com.oguzhanozgokce.androidbootcampfinalproject.data.UserDataStore
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : ThemeRepository {

    override suspend fun setDarkTheme(isDark: Boolean) {
        userDataStore.setDarkTheme(isDark)
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return userDataStore.isDarkTheme()
    }
}