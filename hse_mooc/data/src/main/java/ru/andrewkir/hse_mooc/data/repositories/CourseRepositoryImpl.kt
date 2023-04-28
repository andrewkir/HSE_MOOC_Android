package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.api.CoursesApi
import ru.andrewkir.hse_mooc.data.network.requests.ReviewRequest

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

    suspend fun getReviews(courseId: String) = protectedApiCall { coursesApi.getReviews(courseId) }

    suspend fun postReview(courseId: String, review: ReviewRequest) = protectedApiCall { coursesApi.postReview(courseId, review) }

    suspend fun removeReview(reviewId: String) = protectedApiCall { coursesApi.deleteReview(reviewId) }
}