package com.ninjafarm.poultrymandi.app.feature.notification.presentation

import com.ninjafarm.poultrymandi.app.feature.notification.domian.model.NotificationItem

data class NotificationState(
    val notifications: List<NotificationItem> = emptyList(),
    val selectedLanguage: String = "English",
    val isLoading: Boolean = false,
    val error: String? = null
)
