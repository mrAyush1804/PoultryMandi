package com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase

import android.util.Log
import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        Log.d("LoginUseCase", "invoke() → email: $email")

        // ✅ Step 1: Pehle login try karo
        val loginResult = repository.login(email, password)

        // ✅ Step 2: Login success → return karo
        if (loginResult.isSuccess) {
            Log.d("LoginUseCase", "✅ Login success")
            return loginResult
        }

        val errorMsg = loginResult.exceptionOrNull()?.message ?: ""
        Log.d("LoginUseCase", "Login failed with: $errorMsg")

        // ✅ Step 3: Sirf "account nahi mila" pe signup karo
        // Wrong password pe signup mat karo
        return if (
            errorMsg.contains("Account nahi mila", ignoreCase = true) ||
            errorMsg.contains("no account", ignoreCase = true) ||
            errorMsg.contains("user not found", ignoreCase = true) ||
            errorMsg.contains("no user", ignoreCase = true)
        ) {
            Log.d("LoginUseCase", "Account nahi hai → signUp() karo")
            repository.signUp(email, password)
        } else {
            // ✅ Wrong password ya koi aur error → wahi return karo
            Log.d("LoginUseCase", "Wrong password ya aur error → login error return")
            loginResult
        }
    }
}