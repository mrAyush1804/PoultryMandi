package com.example.poultrymandi.app.feature.home.data.model

data class DataItem(
    val day: Int,
    val dayLabel: String,
    val month: String,
    val year: Int,
    val isAvailable1: String,
    val isAvailable: Boolean = true
)