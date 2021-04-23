package ru.andrewkir.hse_mooc.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import ru.andrewkir.hse_mooc.network.api.BaseApi
import ru.andrewkir.hse_mooc.network.responses.CoursesResponse

interface CoursesApi : BaseApi {
    @GET("courses")
    suspend fun getCourses(): CoursesResponse
}