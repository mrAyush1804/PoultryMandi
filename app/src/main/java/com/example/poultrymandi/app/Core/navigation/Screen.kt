package com.example.poultrymandi.app.Core.navigation



import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
     object Splash : Screen()

    @Serializable
    object Home : Screen()

    @Serializable
    object Login : Screen()

    @Serializable
    object SignUp : Screen()

    @Serializable
    data class Dashboard(val userId: String = "") : Screen()
}