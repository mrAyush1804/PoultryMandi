package com.example.poultrymandi
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.poultrymandi.app.Core.navigation.AppNavigation
import com.example.poultrymandi.app.Core.ui.animation.TwinLinesAnimationWrapper
import com.example.poultrymandi.app.Core.ui.theme.PoultryMandiTheme
import com.example.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase.VerifyEmailLinkUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var verifyEmailLinkUseCase: VerifyEmailLinkUseCase


    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchFCMToken()
            } else {
                Log.w("FCM", "Notification permission denied by user")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleDeepLink(intent)

        // FCM setup — permission check karo, phir token fetch karo
        requestNotificationPermission()

        // Topic subscribe karo — admin "all_users" topic pe bhejega
        subscribeToFCMTopic()

        setContent {
            PoultryMandiTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                .onSuccess { }
                .onFailure { }
        }
    }


    private fun requestNotificationPermission() {
        // Android 13 (API 33) se upar permission maangni padti hai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already hai — seedha token fetch karo
                    fetchFCMToken()
                }
                else -> {

                    notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
        } else {

            fetchFCMToken()
        }
    }

    private fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token: $token")
                    // Token auto-save PoultryFCMService.onNewToken() mein hoga
                    // Agar chahiye toh yahan Firestore mein bhi save kar sakte ho
                } else {
                    Log.e("FCM", "Token fetch failed: ${task.exception?.message}")
                }
            }
    }

    private fun subscribeToFCMTopic() {
        FirebaseMessaging.getInstance()
            .subscribeToTopic("all_users")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Topic 'all_users' subscribed successfully")
                } else {
                    Log.e("FCM", "Topic subscribe failed: ${task.exception?.message}")
                }
            }
    }
}