package com.example.poultrymandi.app.Core.Fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.poultrymandi.MainActivity
import com.example.poultrymandi.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.text.get

@AndroidEntryPoint
class PoultryFCMService : FirebaseMessagingService() {

    private lateinit var contentTitle: String

    @Inject
    lateinit var firestore: FirebaseFirestore

    companion object {
        private const val TAG = "PoultryFCM"
        const val CHANNEL_ID = "poultry_notifications"
        const val CHANNEL_NAME = "Poultry Mandi Alerts"
    }

    /**
     * Jab app install hone ke baad ya token refresh ho tab naya FCM token milta hai.
     * Ise Firestore mein save karo taaki admin target kar sake.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Token: $token")
        saveFCMTokenToFirestore(token)
    }

    /**
     * Jab notification aaye (foreground + data payload background)
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "From: ${message.from}")

        // Firestore mein bhi save karo (existing flow ke liye)
        saveNotificationToFirestore(message)

        // Device pe notification show karo
        showLocalNotification(
            title = message.notification?.title ?: message.data["title"] ?: "Poultry Mandi",
            body  = message.notification?.body  ?: message.data["body"]  ?: "",
            type  = message.data["type"] ?: "PRICE_ALERT",
            city  = message.data["city"],
            rate  = message.data["rate"]
        )
    }

    // ── Private helpers ──────────────────────────────────────────────

    private fun saveFCMTokenToFirestore(token: String) {
        // Users collection mein token save karo (user ID baad mein add karo)
        firestore.collection("fcm_tokens")
            .document(token)
            .set(mapOf(
                "token"     to token,
                "updatedAt" to System.currentTimeMillis(),
                "platform"  to "android"
            ))
            .addOnFailureListener { e ->
                Log.e(TAG, "Token save failed: ${e.message}")
            }
    }

    private fun saveNotificationToFirestore(message: RemoteMessage) {
        val data = message.data
        val notification = message.notification

        val payload = hashMapOf(
            "title"      to (notification?.title ?: data["title"] ?: ""),
            "body"       to (notification?.body  ?: data["body"]  ?: ""),
            "type"       to (data["type"] ?: "PRICE_ALERT"),
            "city"       to (data["city"] ?: ""),
            "dateLabel"  to (data["dateLabel"] ?: ""),
            "rate"       to (data["rate"] ?: ""),
            "isRead"     to false,
            "createdAt"  to System.currentTimeMillis()
        )

        firestore.collection("notifications")
            .add(payload)
            .addOnFailureListener { e ->
                Log.e(TAG, "Firestore save failed: ${e.message}")
            }
    }

    private fun showLocalNotification(
        title: String,
        body: String,
        type: String,
        city: String?,
        rate: String?
    ) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android 8+ ke liye channel zaroori hai
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Price alerts and updates from Poultry Mandi"
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
        }

        // Tap karne pe app open ho
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("navigate_to", "notifications") // MainActivity mein handle karo
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // PRICE_ALERT ke liye extra details dikhao
        val bigText = buildString {
            append(body)
            if (!city.isNullOrBlank()) append("\n📍 City: $city")
            if (!rate.isNullOrBlank()) append("  💰 Rate: $rate")
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.chickes__1_) // apna icon daalo
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}