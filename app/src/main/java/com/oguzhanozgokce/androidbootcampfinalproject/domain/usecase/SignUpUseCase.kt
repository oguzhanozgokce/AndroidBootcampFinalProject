package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import android.util.Patterns
import com.oguzhanozgokce.androidbootcampfinalproject.common.exception.BadRequestException
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        if (email.isBlank()) {
            return Result.failure(BadRequestException("E-posta adresi boş olamaz"))
        }

        if (password.isBlank()) {
            return Result.failure(BadRequestException("Şifre boş olamaz"))
        }

        if (displayName.isBlank()) {
            return Result.failure(BadRequestException("İsim boş olamaz"))
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(BadRequestException("Geçersiz e-posta adresi"))
        }

        if (password.length < 6) {
            return Result.failure(BadRequestException("Şifre en az 6 karakter olmalı"))
        }

        return authRepository.signUpWithEmailAndPassword(email, password, displayName)
    }
}