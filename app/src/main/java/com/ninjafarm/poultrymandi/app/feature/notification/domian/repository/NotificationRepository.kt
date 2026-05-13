package com.ninjafarm.poultrymandi.app.feature.notification.domian.repository

import com.ninjafarm.poultrymandi.app.feature.notification.domian.model.NotificationItem
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<NotificationItem>>
    suspend fun markAsRead(id: String)
}