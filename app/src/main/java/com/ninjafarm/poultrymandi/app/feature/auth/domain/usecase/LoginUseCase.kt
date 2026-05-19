package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase

import android.util.Log
import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        Log.d("LoginUseCase", "invoke() → email: $email")

        // Step 1: Pehle check karo email Firebase mein registered hai ya nahi
        val emailExists = repository.checkEmailExists(email.trim())
        Log.d("LoginUseCase", "emailExists: $emailExists")

        if (!emailExists) {
            // Email registered nahi hai
            Log.d("LoginUseCase", "Email not registered → reject karo")
            return Result.failure(
                Exception("NOT_REGISTERED")
            )
        }

        // Step 2: Email registered hai → login try karo
        Log.d("LoginUseCase", "Email exists → login karo")
        return repository.login(email.trim(), password.trim())
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            // Pehle check karo email registered hai ya nahi
            val exists = repository.checkEmailExists(email)

            if (!exists) {
                // Email Firebase mein nahi hai → unregistered
                return Result.failure(Exception("NOT_REGISTERED"))
            }

            // Email registered hai → reset link bhejo
            repository.sendPasswordResetLink(email)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
