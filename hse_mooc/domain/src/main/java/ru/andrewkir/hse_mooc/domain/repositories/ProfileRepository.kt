package ru.andrewkir.hse_mooc.domain.repositories

import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.FavoriteCoursesResponse
import ru.andrewkir.hse_mooc.domain.network.responses.ViewedCoursesResponse

interface ProfileRepository {
    suspend fun getFavoritesCourses(): ApiResponse<FavoriteCoursesResponse>
    suspend fun getViewedCourses(): ApiResponse<ViewedCoursesResponse>
}