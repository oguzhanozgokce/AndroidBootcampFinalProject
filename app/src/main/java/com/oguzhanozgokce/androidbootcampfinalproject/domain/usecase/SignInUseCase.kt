package com.oguzhanozgokce.androidbootcampfinalproject.domain.usecase

import com.oguzhanozgokce.androidbootcampfinalproject.common.Resource
import com.oguzhanozgokce.androidbootcampfinalproject.domain.model.User
import com.oguzhanozgokce.androidbootcampfinalproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        // Basic validation
        if (email.isBlank()) {
            return Resource.Error("E-posta adresi boş olamaz", errorCode = "missing-email")
        }

        if (password.isBlank()) {
            return Resource.Error("Şifre boş olamaz", errorCode = "missing-password")
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.Error("Geçersiz e-posta adresi", errorCode = "invalid-email")
        }

        return authRepository.signInWithEmailAndPassword(email, password)
    }
}