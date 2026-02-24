package com.example.poultrymandi.app.feature.profile.domain.model

data class UserProfile(
    val name: String,
    val role: String,
    val location: String,
    val profileImageUrl: String? = null,
    val farmerSince: String
)
