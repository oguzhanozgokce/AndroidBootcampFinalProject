package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import android.util.Patterns
import com.oguzhanozgokce.androidbootcampfinalproject.common.exception.BadRequestException
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) {
            return Result.failure(BadRequestException("E-posta adresi boş olamaz"))
        }

        if (password.isBlank()) {
            return Result.failure(BadRequestException("Şifre boş olamaz"))
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(BadRequestException("Geçersiz e-posta adresi"))
        }

        return authRepository.signInWithEmailAndPassword(email, password)
    }
}