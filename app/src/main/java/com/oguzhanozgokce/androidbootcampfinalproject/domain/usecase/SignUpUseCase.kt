package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import android.util.Patterns
import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
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
    ): Resource<User> {
        if (email.isBlank()) {
            return Resource.Error("E-posta adresi boş olamaz", errorCode = "missing-email")
        }

        if (password.isBlank()) {
            return Resource.Error("Şifre boş olamaz", errorCode = "missing-password")
        }

        if (displayName.isBlank()) {
            return Resource.Error("İsim boş olamaz", errorCode = "missing-display-name")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error("Geçersiz e-posta adresi", errorCode = "invalid-email")
        }

        if (password.length < 6) {
            return Resource.Error("Şifre en az 6 karakter olmalı", errorCode = "weak-password")
        }

        return authRepository.signUpWithEmailAndPassword(email, password, displayName)
    }
}