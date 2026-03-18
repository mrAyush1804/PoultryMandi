package com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase

import com.example.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SendEmailLinkUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String): Result<Unit> {
        authRepository.saveEmailLocally(email)
        return authRepository.sendEmailVerificationLink(email)
    }
}