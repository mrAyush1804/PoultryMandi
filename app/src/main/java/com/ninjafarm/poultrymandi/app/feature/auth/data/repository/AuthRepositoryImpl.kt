package com.ninjafarm.poultrymandi.app.feature.auth.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
        private const val USERS = "users"
    }

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            Log.d(TAG, "login() START → email: $email")

            val cleanEmail    = email.trim()
            val cleanPassword = password.trim()

            // Step 1: Normal email+password login try karo
            val result = auth.signInWithEmailAndPassword(cleanEmail, cleanPassword).await()
            val userId = result.user?.uid ?: throw Exception("User ID null hai")
            Log.d(TAG, "login() ✅ SUCCESS → userId: $userId")
            Result.success(userId)

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(TAG, "login() ❌ InvalidCredentials → ${e.errorCode}")

            // Step 2: Google account detect karo — Email/Password link karo
            return try {
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    // Email/Password credential banao aur link karo
                    val emailCredential = EmailAuthProvider
                        .getCredential(email.trim(), password.trim())

                    currentUser.linkWithCredential(emailCredential).await()
                    Log.d(TAG, "login() ✅ Email/Password linked successfully")

                    val userId = currentUser.uid
                    Result.success(userId)

                } else {
                    // Koi user logged in nahi — password reset bhejo
                    auth.sendPasswordResetEmail(email.trim()).await()
                    Log.d(TAG, "login() → Password reset email bheja")
                    Result.failure(Exception("PASSWORD_RESET_SENT"))
                }

            } catch (linkEx: Exception) {
                Log.e(TAG, "login() link failed → ${linkEx.message}")

                when {
                    // Already linked hai — direct login try karo
                    linkEx.message?.contains("already-in-use", ignoreCase = true) == true ||
                    linkEx.message?.contains("already linked", ignoreCase = true) == true -> {
                        return try {
                            val retryResult = auth
                                .signInWithEmailAndPassword(email.trim(), password.trim())
                                .await()
                            val userId = retryResult.user?.uid
                                ?: throw Exception("User ID null")
                            Result.success(userId)
                        } catch (retryEx: Exception) {
                            Result.failure(Exception("Wrong password"))
                        }
                    }
                    else -> Result.failure(Exception("Wrong password"))
                }
            }

        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(TAG, "login() ❌ User not found → ${e.errorCode}")
            Result.failure(Exception("Account nahi mila"))

        } catch (e: Exception) {
            Log.e(TAG, "login() ❌ ${e.javaClass.simpleName}: ${e.message}")
            Result.failure(e)
        }
    }


    override suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            Log.d(TAG, "signUp() → START email: $email")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID null hai")


            firestore.collection(USERS)
                .document(userId)
                .set(mapOf(
                    "uid"          to userId,
                    "email"        to email,
                    "name"         to "",
                    "phone"        to "",
                    "occupation"   to "",
                    "income"       to "",
                    "address"      to "",
                    "subscription" to "free",
                    "createdAt"    to System.currentTimeMillis()
                ), SetOptions.merge()).await()

            Log.d(TAG, "signUp() →  SUCCESS userId: $userId")
            Result.success(userId)
        } catch (e: Exception) {
            Log.e(TAG, "signUp() →  ${e.message}", e)
            Result.failure(e)
        }
    }


    override suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val methods = auth.fetchSignInMethodsForEmail(email).await()
            val exists = methods.signInMethods?.isNotEmpty() == true
            Log.d(TAG, "checkEmailExists() → $exists for $email")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "checkEmailExists() → error: ${e.message}")
            false
        }
    }

    override suspend fun sendEmailVerificationLink(email: String): Result<Unit> {
        return try {
            val actionCodeSettings = actionCodeSettings {
                url = "https://poultrymandi.firebaseapp.com/finishSignUp"
                handleCodeInApp = true
                setAndroidPackageName("com.ninjafarm.poultrymandi", true, "21")
            }
            auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmailLink(email: String, link: String): Result<Unit> {
        return try {
            Firebase.auth.signInWithEmailLink(email, link).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isEmailSignInLink(link: String): Boolean = auth.isSignInWithEmailLink(link)

    override fun saveEmailLocally(email: String) {
        sharedPreferences.edit().putString("pending_email", email).apply()
    }

    override fun getSavedEmail(): String? = sharedPreferences.getString("pending_email", null)

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val isNewUser = result.additionalUserInfo?.isNewUser ?: false

            if (isNewUser) {
                val user = auth.currentUser
                firestore.collection(USERS)
                    .document(user?.uid ?: return Result.failure(Exception("User null hai")))
                    .set(mapOf(
                        "uid"          to user?.uid,
                        "name"         to (user?.displayName ?: ""),
                        "email"        to (user?.email ?: ""),
                        "photoUrl"     to (user?.photoUrl?.toString() ?: ""),
                        "phone"        to "",
                        "occupation"   to "",
                        "income"       to "",
                        "address"      to "",
                        "subscription" to "free",
                        "createdAt"    to System.currentTimeMillis()
                    )).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithGoogle → ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun saveCompleteProfile(
        uid: String, name: String, phone: String,
        occupation: String, income: String, address: String
    ): Result<Unit> {
        return try {
            firestore.collection(USERS).document(uid)
                .set(mapOf(
                    "name"       to name,
                    "phone"      to phone,
                    "occupation" to occupation,
                    "income"     to income,
                    "address"    to address,
                    "profileCompletedAt" to System.currentTimeMillis()
                ), SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isProfileComplete(): Boolean {
        return try {
            val uid = auth.currentUser?.uid ?: return false
            val doc = firestore.collection(USERS).document(uid).get().await()
            val phone = doc.getString("phone") ?: ""
            phone.isNotBlank()
        } catch (e: Exception) {
            false
        }
    }
}
