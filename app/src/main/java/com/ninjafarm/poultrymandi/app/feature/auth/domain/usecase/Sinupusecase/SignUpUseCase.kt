package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase

import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email   : String,
        password: String
    ): Result<String> {
        return repository.signUp(email.trim(), password.trim())
    }
}
