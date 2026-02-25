package com.example.poultrymandi.app.feature.notification.domian.model

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val city: String? = null,
    val state: String? = null,
    val rate: String? = null,
    val date: String? = null,
    val isRead: Boolean = false
)

enum class NotificationType {
    PRICE_ALERT, SYSTEM_UPDATE, PROMOTION
}
