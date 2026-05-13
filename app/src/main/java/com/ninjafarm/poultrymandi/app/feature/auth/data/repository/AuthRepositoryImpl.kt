package com.ninjafarm.poultrymandi.app.feature.auth.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.ninjafarm.poultrymandi.app.feature.auth.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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
    override suspend fun sendEmailVerificationLink(email: String): Result<Unit> {
        return try {
            val actionCodeSettings = actionCodeSettings {
                url = "https://poultrymandi.firebaseapp.com/finishSignUp"
                handleCodeInApp = true
                setAndroidPackageName(
                    "com.example.poultrymandi",
                    true,
                    "21"
                )
            }
            auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
            Result.success(Unit)

        }catch (e: Exception){
            Result.failure(e)

        } as Result<Unit>
    }

    override suspend fun signInWithEmailLink(
        email: String,
        link: String
    ): Result<Unit> {

        return try {
            Firebase.auth
                .signInWithEmailLink(email, link)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override fun isEmailSignInLink(link: String): Boolean {
        return auth.isSignInWithEmailLink(link)
    }

    override fun saveEmailLocally(email: String) {
        sharedPreferences.edit().putString("pending_email", email).apply()
    }

    override fun getSavedEmail(): String? {
        return sharedPreferences.getString("pending_email", null)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val isNewUser = result.additionalUserInfo?.isNewUser ?: false


            if(isNewUser){
                val user = auth.currentUser
                Log.d(TAG, "New Google User → Firestore mein basic data save karo")
                firestore.collection(USERS)
                    .document(user?.uid ?: return Result.failure(
                        Exception("User logged in nahi hai")
                    ))
                    .set(mapOf(
                        "uid"        to user?.uid,
                        "name"       to (user?.displayName ?: ""),
                        "email"      to (user?.email ?: ""),
                        "photoUrl"   to (user?.photoUrl?.toString() ?: ""),
                        "phone"      to "",
                        "occupation" to "",
                        "income"     to "",
                        "address"    to "",
                        "subscription" to "free",
                        "createdAt"  to System.currentTimeMillis()
                    )).await()

                Log.d(TAG, "✅ Basic profile saved")

            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithGoogle →  ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun saveCompleteProfile(
        uid: String,
        name: String,
        phone: String,
        occupation: String,
        income: String,
        address: String
    ): Result<Unit> {
        return try {
            Log.d(TAG, "saveCompleteProfile → START uid: $uid")

            // Changed .update() to .set() with merge to ensure document is created if it doesn't exist
            firestore.collection(USERS)
                .document(uid)
                .set(mapOf(
                    "name"       to name,
                    "phone"      to phone,
                    "occupation" to occupation,
                    "income"     to income,
                    "address"    to address,
                    "profileCompletedAt" to System.currentTimeMillis()
                ), SetOptions.merge()).await()

            Log.d(TAG, " Profile saved to Firestore")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, " Profile save failed: ${e.message}", e)
            Result.failure(e)
        }

    }

    override suspend fun isProfileComplete(): Boolean {
        return try {
            val uid = auth.currentUser?.uid ?: return false

            val doc = firestore.collection(USERS)
                .document(uid)
                .get()
                .await()

            val phone = doc.getString("phone") ?: ""
            val isComplete = phone.isNotBlank()

            Log.d(TAG, "isProfileComplete → $isComplete | phone: $phone")
            isComplete

        } catch (e: Exception) {
            Log.e(TAG, "isProfileComplete check failed: ${e.message}")
            false
        }    }


}