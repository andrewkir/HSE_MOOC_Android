package ru.andrewkir.hse_mooc.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.andrewkir.hse_mooc.network.api.BaseApi
import ru.andrewkir.hse_mooc.network.responses.CoursesResponse

interface CoursesApi : BaseApi {
    @GET("courses")
    suspend fun getCourses(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("searchQuery") searchQuery: String = ""
    ): CoursesResponse
    
    @POST("/auth/auth-test")
    suspend fun testAuth(): ResponseBody
}