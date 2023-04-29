package ru.andrewkir.hse_mooc.domain.network.api

import okhttp3.ResponseBody
import retrofit2.http.*
import ru.andrewkir.hse_mooc.data.network.api.BaseApi
import ru.andrewkir.hse_mooc.data.network.requests.ReviewRequest
import ru.andrewkir.hse_mooc.domain.network.responses.Categories.CategoriesResponse
import ru.andrewkir.hse_mooc.domain.responses.Compilations.CompilationsResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Course.CourseResponse
import ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview.CoursesPreviewResponse
import ru.andrewkir.hse_mooc.domain.network.responses.FavoriteCoursesResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Reviews.ReviewsResponse
import ru.andrewkir.hse_mooc.domain.network.responses.ViewedCoursesResponse

interface CoursesApi : BaseApi {
    @GET("courses")
    suspend fun getCourses(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("searchQuery") searchQuery: String = "",
        @Query("categories") categories: String = ""
    ): CoursesPreviewResponse

    @GET("courses/{id}")
    suspend fun getCourse(
        @Path(value = "id", encoded = true) id: String
    ): CourseResponse

    @GET("users/favourite")
    suspend fun getFavorites(): FavoriteCoursesResponse

    @POST("users/favourite")
    suspend fun addToFavourites(
        @Query("id") courseId: String
    ): ResponseBody

    @DELETE("users/favourite")
    suspend fun deleteFromFavourites(
        @Query("id") courseId: String
    ): ResponseBody

    @GET("users/viewed")
    suspend fun getViewed(): ViewedCoursesResponse

    @POST("users/viewed")
    suspend fun addToViewed(
        @Query("id") courseId: String
    ): ResponseBody

    @DELETE("users/viewed")
    suspend fun deleteFromViewed(
        @Query("id") courseId: String
    ): ResponseBody

    @GET("reviews")
    suspend fun getReviews(
        @Query("id") courseId: String
    ): ReviewsResponse

    @POST("reviews")
    suspend fun postReview(
        @Query("id") courseId: String,
        @Body body: ReviewRequest
    ): ResponseBody

    @DELETE("reviews")
    suspend fun deleteReview(
        @Query("reviewId") reviewId: String
    ): ResponseBody

    @GET("categories")
    suspend fun getCategories(): CategoriesResponse

    @GET("courses/main")
    suspend fun getMainCourses(): CoursesPreviewResponse

    @GET("/compilations/")
    suspend fun getCompilations(): CompilationsResponse

    @GET
    suspend fun getTrending(@Url url: String): CoursesPreviewResponse
}