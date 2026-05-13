package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase



import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<Unit> {
        return authRepository.signInWithGoogle(idToken)
    }
}