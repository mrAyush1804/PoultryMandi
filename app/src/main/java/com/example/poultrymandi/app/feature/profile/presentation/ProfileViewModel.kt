package com.example.poultrymandi.app.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poultrymandi.app.feature.profile.domain.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {

        val mockProfile = UserProfile(
            name = "Rajesh Kumar",
            role = "Poultry Farmer since 2018",
            location = "Pune, Maharashtra",
            farmerSince = "2018"
        )
        _uiState.update { it.copy(userProfile = mockProfile) }
    }

    fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()  // ✅ Signs out from Firebase
                _logoutEvent.value = true  // ✅ Notify UI to navigate
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Logout failed: ${e.message}") }
            }
        }
    }




}
