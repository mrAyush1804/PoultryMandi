package com.example.poultrymandi.app.feature.notification.presentation

import androidx.lifecycle.ViewModel
import com.example.poultrymandi.app.feature.notification.domian.model.NotificationItem
import com.example.poultrymandi.app.feature.notification.domian.model.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())
    val uiState: StateFlow<NotificationState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        // Mock data for notifications
        val mockNotifications = listOf(
            NotificationItem(
                id = "1",
                title = "Price Alert: Eggs",
                message = "Rates in Indore have increased by ₹0.15.",
                timestamp = "10:30 AM",
                type = NotificationType.PRICE_ALERT,
                city = "Indore",
                state = "MP",
                rate = "₹4.55",
                date = "12 Oct 2023"
            ),
            NotificationItem(
                id = "2",
                title = "New System Update",
                message = "Version 2.0 is now available with improved charts.",
                timestamp = "Yesterday",
                type = NotificationType.SYSTEM_UPDATE,
                date = "11 Oct 2023"
            ),
            NotificationItem(
                id = "3",
                title = "Price Alert: Broiler",
                message = "Special rates for bulk orders in Pune.",
                timestamp = "2 days ago",
                type = NotificationType.PRICE_ALERT,
                city = "Pune",
                state = "Maharashtra",
                rate = "₹95.00",
                date = "10 Oct 2023"
            )
        )
        _uiState.update { it.copy(notifications = mockNotifications) }
    }

    fun markAsRead(id: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }
            )
        }
    }

    fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }
}
