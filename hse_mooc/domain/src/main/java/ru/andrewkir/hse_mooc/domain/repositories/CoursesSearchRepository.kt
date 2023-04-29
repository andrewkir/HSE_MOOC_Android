package ru.andrewkir.hse_mooc.domain.repositories

import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Categories.CategoriesResponse
import ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview.CoursesPreviewResponse

interface CoursesSearchRepository {
    suspend fun getCategories(): ApiResponse<CategoriesResponse>
    suspend fun getCoursesFromServer(
        query: String,
        currentPage: Int,
        categories: String = ""
    ): ApiResponse<CoursesPreviewResponse>
}