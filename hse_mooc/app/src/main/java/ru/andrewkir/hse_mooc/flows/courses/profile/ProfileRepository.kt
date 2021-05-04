package ru.andrewkir.hse_mooc.flows.courses.profile

import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.CoursesApi

class ProfileRepository(
    private val coursesApi: CoursesApi
) : BaseRepository() {
    suspend fun getFavoritesCourses() = protectedApiCall {
        coursesApi.getFavorites()
    }

    suspend fun getViewedCourses() = protectedApiCall {
        coursesApi.getViewed()
    }
}