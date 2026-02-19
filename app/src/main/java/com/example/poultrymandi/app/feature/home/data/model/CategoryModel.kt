package com.example.poultrymandi.app.feature.home.data.model

// data/model/CategoryModel.kt
data class CategoryModel(
    val id: String,
    val title: String,
    val icon: Int,
    val isSelected: Boolean = false
)