package com.ninjafarm.poultrymandi.app.feature.auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun signUp(email: String, password: String): Result<String>  // ✅ Naya
    suspend fun checkEmailExists(email: String): Boolean                 // ✅ Naya
    suspend fun sendEmailVerificationLink(email: String): Result<Unit>
    suspend fun signInWithEmailLink(email: String, link: String): Result<Unit>
    fun isEmailSignInLink(link: String): Boolean
    fun saveEmailLocally(email: String)
    fun getSavedEmail(): String?
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun saveCompleteProfile(
        uid: String,
        name: String,
        phone: String,
        occupation: String,
        income: String,
        address: String
    ): Result<Unit>
    suspend fun isProfileComplete(): Boolean
}