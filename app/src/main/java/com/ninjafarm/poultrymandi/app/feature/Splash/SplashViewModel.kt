package com.ninjafarm.poultrymandi.app.feature.Splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Loading)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    private var authListener: FirebaseAuth.AuthStateListener? = null

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        // Bug 1 Fix: Use AuthStateListener to reliably detect session restoration on cold start
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            // Remove listener immediately after first callback fires
            removeListener()

            val user = firebaseAuth.currentUser

            if (user == null) {
                _destination.value = SplashDestination.Login
                return@AuthStateListener
            }

            // Logged in -> check Firestore profile (existing logic)
            viewModelScope.launch {
                try {
                    val doc = firestore
                        .collection("users")
                        .document(user.uid)
                        .get()
                        .await()

                    val phone = doc.getString("phone") ?: ""
                    val name = doc.getString("name") ?: ""

                    _destination.value = if (phone.isBlank() || name.isBlank()) {
                        SplashDestination.CompleteProfile
                    } else {
                        SplashDestination.Home
                    }
                } catch (e: Exception) {
                    _destination.value = SplashDestination.Login
                }
            }
        }

        auth.addAuthStateListener(authListener!!)
    }

    private fun removeListener() {
        authListener?.let {
            auth.removeAuthStateListener(it)
            authListener = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        removeListener() // Memory leak prevention
    }
}

sealed class SplashDestination {
    object Loading : SplashDestination()
    object Login : SplashDestination()
    object Home : SplashDestination()
    object CompleteProfile : SplashDestination()
}
