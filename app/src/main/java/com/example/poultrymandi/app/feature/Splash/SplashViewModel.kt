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

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            val user = auth.currentUser

            // Not logged in → go to Login
            if (user == null) {
                _destination.value = SplashDestination.Login
                return@launch
            }

            try {
                // Check if user already has profile in Firestore
                val doc = firestore
                    .collection("users")
                    .document(user.uid)
                    .get()
                    .await()

                val phone = doc.getString("phone") ?: ""
                val name = doc.getString("name") ?: ""

                if (phone.isBlank() || name.isBlank()) {
                    // Profile incomplete → go to CompleteProfile
                    _destination.value = SplashDestination.CompleteProfile
                } else {
                    // Profile complete → go directly to Home
                    _destination.value = SplashDestination.Home
                }
            } catch (e: Exception) {
                // Firestore error or network issue → go to Login to be safe
                _destination.value = SplashDestination.Login
            }
        }
    }
}

// Possible splash destinations
sealed class SplashDestination {
    object Loading : SplashDestination()
    object Login : SplashDestination()
    object Home : SplashDestination()
    object CompleteProfile : SplashDestination()
}
