package com.example.poultrymandi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.poultrymandi.app.Core.navigation.AppNavigation
import com.example.poultrymandi.app.Core.ui.animation.TwinLinesAnimationWrapper
import com.example.poultrymandi.app.Core.ui.theme.PoultryMandiTheme
import com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase.VerifyEmailLinkUseCase
import com.example.poultrymandi.app.feature.auth.presentation.login.LoginScreen
import com.example.poultrymandi.app.feature.auth.presentation.signup.SignupScreen
import com.example.poultrymandi.app.feature.home.presentation.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var verifyEmailLinkUseCase: VerifyEmailLinkUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleDeepLink(intent)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val emailLink = intent.data?.toString() ?: return

        lifecycleScope.launch {
            verifyEmailLinkUseCase(emailLink)
                .onSuccess {

                }
                .onFailure {

                }
        }
    }
}