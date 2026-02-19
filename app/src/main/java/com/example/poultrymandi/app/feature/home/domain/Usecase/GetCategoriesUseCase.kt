package com.example.poultrymandi.app.feature.home.domain.Usecase

import com.example.poultrymandi.app.feature.home.data.repository.CategoryRepository
import com.example.poultrymandi.app.feature.home.domain.data.CategoryDomain
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(): List<CategoryDomain> {
        return repository.getCategories().map { model ->
            CategoryDomain(
                id = model.id,
                title = model.title,
                icon = model.icon
            )
        }
    }
}