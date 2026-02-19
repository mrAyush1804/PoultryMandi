package com.example.poultrymandi.app.feature.home.data.repository

import com.example.poultrymandi.app.feature.home.data.Enums.Category
import com.example.poultrymandi.app.feature.home.data.model.CategoryModel

// data/repository/CategoryRepository.kt
class CategoryRepository {
    fun getCategories(): List<CategoryModel> {
        return Category.values().map { it.toModel() }
    }
}g