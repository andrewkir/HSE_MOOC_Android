package ru.andrewkir.hse_mooc.network.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.andrewkir.hse_mooc.network.api.BaseApi
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

interface AuthApi : BaseApi {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse
}