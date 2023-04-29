package ru.andrewkir.hse_mooc.domain.repositories

import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.data.network.requests.ReviewRequest
import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Course.CourseResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Reviews.ReviewsResponse

interface CourseRepository {
    suspend fun getCourse(id: String): ApiResponse<CourseResponse>
    suspend fun updateFavorites(id: String, isLiked: Boolean): ApiResponse<ResponseBody>
    suspend fun updateViewed(id: String, isViewed: Boolean): ApiResponse<ResponseBody>
    suspend fun getReviews(courseId: String): ApiResponse<ReviewsResponse>
    suspend fun postReview(courseId: String, review: ReviewRequest): ApiResponse<ResponseBody>
    suspend fun removeReview(reviewId: String): ApiResponse<ResponseBody>
}