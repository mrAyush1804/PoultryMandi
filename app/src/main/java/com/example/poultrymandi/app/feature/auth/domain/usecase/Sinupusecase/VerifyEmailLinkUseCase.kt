package com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase

import com.example.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyEmailLinkUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(link: String): Result<Unit> {
        if (!authRepository.isEmailSignInLink(link)) {
            return Result.failure(Exception("Invalid link"))
        }


        val email = authRepository.getSavedEmail()
            ?: return Result.failure(Exception("Email not found, please enter again"))

        return authRepository.signInWithEmailLink(email, link)
    }
}