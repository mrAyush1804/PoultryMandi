package com.ninjafarm.poultrymandi.app.feature.home.data.Enums

import com.ninjafarm.poultrymandi.R
import com.ninjafarm.poultrymandi.app.feature.home.data.model.CategoryModel

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