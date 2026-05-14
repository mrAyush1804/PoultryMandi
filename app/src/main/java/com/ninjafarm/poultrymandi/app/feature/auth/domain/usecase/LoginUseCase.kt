package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase

import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return repository.login(email, password)
    }
}