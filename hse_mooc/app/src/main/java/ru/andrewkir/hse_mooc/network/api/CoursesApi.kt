package ru.andrewkir.hse_mooc.network.api

import okhttp3.ResponseBody
import retrofit2.http.*
import ru.andrewkir.hse_mooc.network.responses.Categories.CategoriesResponse
import ru.andrewkir.hse_mooc.network.responses.Course.CourseResponse
import ru.andrewkir.hse_mooc.network.responses.CoursesPreview.CoursesPreviewResponse

interface CoursesApi : BaseApi {
    @GET("courses")
    suspend fun getCourses(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("searchQuery") searchQuery: String = "",
        @Query("categories") categories: String = ""
    ): CoursesPreviewResponse

    @GET("/courses/{id}")
    suspend fun getCourse(
        @Path(value = "id", encoded = true) id: String
    ): CourseResponse

    @POST("/users/favourite")
    suspend fun addToFavourites(
        @Query("id") courseId: String
    ): ResponseBody

    @DELETE("/users/favourite")
    suspend fun deleteFromFavourites(
        @Query("id") courseId: String
    ): ResponseBody

    @POST("/users/viewed")
    suspend fun addToViewed(
        @Query("id") courseId: String
    ): ResponseBody

    @DELETE("/users/viewed")
    suspend fun deleteFromViewed(
        @Query("id") courseId: String
    ): ResponseBody

    @POST("/auth/auth-test")
    suspend fun testAuth(): ResponseBody

    @GET("categories")
    suspend fun getCategories(): CategoriesResponse
}