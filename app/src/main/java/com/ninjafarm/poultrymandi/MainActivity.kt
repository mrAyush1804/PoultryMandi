package com.ninjafarm.poultrymandi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.ninjafarm.poultrymandi.app.Core.navigation.AppNavigation
import com.ninjafarm.poultrymandi.app.Core.ui.animation.TwinLinesAnimationWrapper
import com.ninjafarm.poultrymandi.app.Core.ui.theme.PoultryMandiTheme
import com.ninjafarm.poultrymandi.app.feature.auth.domain.usecase.Sinupusecase.VerifyEmailLinkUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var verifyEmailLinkUseCase: VerifyEmailLinkUseCase

    // Play In-App Update Manager
    private lateinit var appUpdateManager: AppUpdateManager
    private val REQ_CODE_VERSION_UPDATE = 5321

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

        // In-App Update Manager Initialize karein
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForUpdates()

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

    override fun onResume() {
        super.onResume()
        // Agar user mandatory update screen se bahar nikalta hai ya minimize karta hai,
        // to check karo ki update abhi bhi progress me to nahi hai. Agar hai to wapas full-screen pop-up layo.
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        this,
                        AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE),
                        REQ_CODE_VERSION_UPDATE
                    )
                } catch (e: Exception) {
                    Log.e("Update", "Resume update flow failed: ${e.message}")
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) {
                // Agar user update 'Cancel' kar deta hai ya network issue se update fail hota hai
                // Kyunki ye full blocking/mandatory update hai, hum toast dekar app close kar denge.
                Toast.makeText(this, "Poultry Mandi ko chalane ke liye update karna zaroori hai!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun checkForUpdates() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            // Agar Play Store par update available hai AND 'Immediate' (full screen) update allowed hai
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        this,
                        AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE),
                        REQ_CODE_VERSION_UPDATE
                    )
                } catch (e: Exception) {
                    Log.e("Update", "Starting update flow failed: ${e.message}")
                }
            }
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
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

    private fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token: $token")

                    FirebaseFirestore.getInstance()
                        .collection("fcm_tokens")
                        .document(token)
                        .set(mapOf(
                            "token"     to token,
                            "updatedAt" to System.currentTimeMillis(),
                            "platform"  to "android"
                        ))
                        .addOnSuccessListener {
                            Log.d("FCM", "Token saved to Firestore")
                        }
                        .addOnFailureListener {
                            Log.e("FCM", "Token save failed: ${it.message}")
                        }

                } else {
                    Log.e("FCM", "Token fetch failed: ${task.exception?.message}")
                }
            }
    }
}