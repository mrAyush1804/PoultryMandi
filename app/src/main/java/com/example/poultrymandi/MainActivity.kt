package com.example.poultrymandi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.poultrymandi.app.Core.navigation.AppNavigation
import com.example.poultrymandi.app.Core.ui.animation.TwinLinesAnimationWrapper
import com.example.poultrymandi.app.Core.ui.theme.PoultryMandiTheme
import com.example.poultrymandi.app.feature.auth.presentation.login.LoginScreen
import com.example.poultrymandi.app.feature.auth.presentation.signup.SignupScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PoultryMandiTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->

                    TwinLinesAnimationWrapper(
                        content = {
                            Box(modifier = Modifier.padding(innerPadding)) {
                                AppNavigation(navController = navController)


                            }
                        },
                        Logo = 1
                    )



                }
            }
        }
    }
}