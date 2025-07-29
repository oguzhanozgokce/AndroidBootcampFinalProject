package com.oguzhanozgokce.androidbootcampfinalproject.di

import com.oguzhanozgokce.androidbootcampfinalproject.data.repository.AuthRepositoryImpl
import com.oguzhanozgokce.androidbootcampfinalproject.data.repository.GameScoreRepositoryImpl
import com.oguzhanozgokce.androidbootcampfinalproject.data.repository.GameSettingsRepositoryImpl
import com.oguzhanozgokce.androidbootcampfinalproject.data.repository.ThemeRepositoryImpl
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameScoreRepository
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.GameSettingsRepository
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameScoreRepository(
        gameScoreRepositoryImpl: GameScoreRepositoryImpl
    ): GameScoreRepository

    @Binds
    @Singleton
    abstract fun bindGameSettingsRepository(
        gameSettingsRepositoryImpl: GameSettingsRepositoryImpl
    ): GameSettingsRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository
}