package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<User?> {
        return authRepository.getCurrentUser()
    }

    fun flow(): Flow<Resource<User?>> {
        return authRepository.getCurrentUserFlow()
    }

    fun isUserSignedIn(): Boolean {
        return authRepository.isUserSignedIn()
    }
}