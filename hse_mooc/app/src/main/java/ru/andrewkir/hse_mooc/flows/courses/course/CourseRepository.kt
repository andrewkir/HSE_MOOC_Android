package ru.andrewkir.hse_mooc.flows.courses.course

import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.CoursesApi

class CourseRepository(
    private val coursesApi: CoursesApi
) : BaseRepository() {

    suspend fun getCourse(id: String) = protectedApiCall {
        coursesApi.getCourse(id)
    }

    suspend fun updateFavorites(id: String, isLiked: Boolean) = protectedApiCall {
        if (isLiked) coursesApi.addToFavourites(id)
        else coursesApi.deleteFromFavourites(id)
    }

    suspend fun updateViewed(id: String, isViewed: Boolean) = protectedApiCall {
        if (isViewed) coursesApi.addToViewed(id)
        else coursesApi.deleteFromViewed(id)
    }
}