package com.example.poultrymandi.app.feature.home.data.Enums

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.poultrymandi.R
import com.example.poultrymandi.app.feature.home.data.model.CategoryModel

enum class Category(
    val title: String,
    val icon: Int
) {
    EGGS("Eggs", R.drawable.egg_tongue_face),
    CHICKEN("Chicken", R.drawable.boiled_chicken),
    BROILER("Broiler", R.drawable.chicken);

    fun toModel(): CategoryModel = CategoryModel(
        id = name.lowercase(),
        title = title,
        icon = icon
    )
}