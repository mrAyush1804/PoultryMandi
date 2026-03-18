package com.example.poultrymandi.app.feature.CompleteProfile.domain.model

data class UserProfileData(
    val name: String,
    val phoneNumber: String,
    val occupation: String,
    val income: String,
    val address: String? = null
)
