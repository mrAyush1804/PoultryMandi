package com.example.poultrymandi.app.feature.profile.presentation

import com.example.poultrymandi.app.feature.profile.domain.model.UserProfile

data class ProfileState(
    val userProfile: UserProfile? = null,
    val languages: List<String> = listOf("English", "Hindi", "Marathi"),
    val selectedLanguage: String = "English",
    val isLoading: Boolean = false,
    val error: String? = null,
    val seedSuccess: Boolean=false
)

sealed class Propilescreenevent() {
    object SeedData : Propilescreenevent()
}
