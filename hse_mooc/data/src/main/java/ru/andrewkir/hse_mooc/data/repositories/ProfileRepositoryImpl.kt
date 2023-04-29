package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.repositories.ProfileRepository

class ProfileRepositoryImpl(
    private val coursesApi: CoursesApi
) : ProfileRepository, BaseRepository() {
    override suspend fun getFavoritesCourses() = protectedApiCall {
        coursesApi.getFavorites()
    }

    override suspend fun getViewedCourses() = protectedApiCall {
        coursesApi.getViewed()
    }
}