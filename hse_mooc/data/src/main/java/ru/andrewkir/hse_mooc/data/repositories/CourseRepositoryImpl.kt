package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.requests.ReviewRequest
import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.repositories.CourseRepository

class CourseRepositoryImpl(
    private val coursesApi: CoursesApi
) : CourseRepository, BaseRepository() {

    override suspend fun getCourse(id: String) = protectedApiCall {
        coursesApi.getCourse(id)
    }

    override suspend fun updateFavorites(id: String, isLiked: Boolean) = protectedApiCall {
        if (isLiked) coursesApi.addToFavourites(id)
        else coursesApi.deleteFromFavourites(id)
    }

    override suspend fun updateViewed(id: String, isViewed: Boolean) = protectedApiCall {
        if (isViewed) coursesApi.addToViewed(id)
        else coursesApi.deleteFromViewed(id)
    }

    override suspend fun getReviews(courseId: String) = protectedApiCall { coursesApi.getReviews(courseId) }

    override suspend fun postReview(courseId: String, review: ReviewRequest) = protectedApiCall { coursesApi.postReview(courseId, review) }

    override suspend fun removeReview(reviewId: String) = protectedApiCall { coursesApi.deleteReview(reviewId) }
}