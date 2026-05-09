package com.example.poultrymandi.app.feature.Splash

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
        // Production-grade Auth flow: Use AuthStateListener to ensure Firebase session is restored from cache.
        // Reading currentUser directly on cold start can return null prematurely before the session is restored.
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            // Remove listener immediately after the first callback to prevent duplicate processing.
            // We only need the initial state to decide where to navigate.
            removeListener()

            val user = firebaseAuth.currentUser
            if (user == null) {
                _destination.value = SplashDestination.Login
                return@AuthStateListener
            }

            viewModelScope.launch {
                try {
                    // Check profile completion status in Firestore
                    val doc = firestore
                        .collection("users")
                        .document(user.uid)
                        .get()
                        .await()

                    if (doc.exists()) {
                        val phone = doc.getString("phone") ?: ""
                        val name = doc.getString("name") ?: ""

                        _destination.value = if (phone.isBlank() || name.isBlank()) {
                            SplashDestination.CompleteProfile
                        } else {
                            SplashDestination.Home
                        }
                    } else {
                        // User exists in Auth but no Firestore record yet
                        _destination.value = SplashDestination.CompleteProfile
                    }
                } catch (e: Exception) {
                    // Fallback to Login if data fetch fails or user is deleted
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
        // Clean up listener to prevent memory leaks
        removeListener()
    }
}

sealed class SplashDestination {
    object Loading : SplashDestination()
    object Login : SplashDestination()
    object Home : SplashDestination()
    object CompleteProfile : SplashDestination()
}
