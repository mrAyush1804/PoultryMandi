package com.ninjafarm.poultrymandi.app.feature.profile.domain.model

data class UserProfile(
    val name: String = "",
    val phone: String = "",
    val occupation: String = "",
    val income: String = "",
    val address: String = "",
    val profileImageUrl: String? = null,
    val farmerSince: String = ""
)
