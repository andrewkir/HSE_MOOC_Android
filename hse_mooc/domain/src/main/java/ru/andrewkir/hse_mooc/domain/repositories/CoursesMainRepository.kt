package ru.andrewkir.hse_mooc.domain.repositories

import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview.CoursesPreviewResponse
import ru.andrewkir.hse_mooc.domain.responses.Compilations.CompilationsResponse

interface CoursesMainRepository {
    suspend fun getCompilations(): ApiResponse<CompilationsResponse>
    suspend fun getCoursesMain(): ApiResponse<CoursesPreviewResponse>
    suspend fun getTrendingCourses(url: String): ApiResponse<CoursesPreviewResponse>
}