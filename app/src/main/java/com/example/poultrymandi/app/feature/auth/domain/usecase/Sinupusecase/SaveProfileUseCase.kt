package com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase

import com.example.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke(
        name: String,
        phone: String,
        occupation: String,
        income: String,
        address: String
    ): Result<Unit> {
        val uid = auth.currentUser?.uid
            ?: return Result.failure(Exception("User logged in nahi hai"))

        return authRepository.saveCompleteProfile(
            uid = uid,
            name = name,
            phone = phone,
            occupation = occupation,
            income = income,
            address = address
        )
    }
}