package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.api.CoursesApi

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