package com.example.poultrymandi.app.feature.notification.data.repository

import android.util.Log
import com.example.poultrymandi.app.feature.notification.domian.model.NotificationItem
import com.example.poultrymandi.app.feature.notification.domian.model.NotificationType
import com.example.poultrymandi.app.feature.notification.domian.repository.NotificationRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    companion object {
        private const val TAG = "NotificationRepo"
    }

    override fun getNotifications(): Flow<List<NotificationItem>> = callbackFlow {
        val listener = firestore
            .collection("notifications")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        val createdAt = doc.getLong("createdAt") ?: 0L
                        NotificationItem(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            message = doc.getString("body") ?: "",
                            timestamp = SimpleDateFormat(
                                "dd MMM, hh:mm a", Locale.ENGLISH
                            ).format(Date(createdAt)),
                            type = when (doc.getString("type")) {
                                "SYSTEM_UPDATE" -> NotificationType.SYSTEM_UPDATE
                                "PROMOTION" -> NotificationType.PROMOTION
                                else -> NotificationType.PRICE_ALERT
                            },
                            city = doc.getString("city"),
                            date = doc.getString("dateLabel"),
                            isRead = doc.getBoolean("isRead") ?: false,
                            createdAt = createdAt
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Parse error: ${e.message}")
                        null
                    }
                } ?: emptyList()

                Log.d(TAG, "Fetched: ${list.size} notifications")
                trySend(list)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun markAsRead(id: String) {
        try {
            firestore.collection("notifications")
                .document(id)
                .update("isRead", true)
        } catch (e: Exception) {
            Log.e(TAG, "markAsRead error: ${e.message}")
        }
    }
}