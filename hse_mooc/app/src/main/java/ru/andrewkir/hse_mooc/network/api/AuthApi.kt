package ru.andrewkir.hse_mooc.network.api

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.andrewkir.hse_mooc.network.api.BaseApi
import ru.andrewkir.hse_mooc.network.requests.LoginEmailRequest
import ru.andrewkir.hse_mooc.network.requests.LoginUsernameRequest
import ru.andrewkir.hse_mooc.network.requests.RegisterRequest
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

interface AuthApi : BaseApi {

    @POST("auth/signin")
    suspend fun loginByUsername(
        @Body body: LoginUsernameRequest
    ): LoginResponse

    @POST("auth/signin")
    suspend fun loginByEmail(
        @Body body: LoginEmailRequest
    ): LoginResponse

    @POST("auth/signup")
    suspend fun register(
        @Body body: RegisterRequest
    ): ResponseBody
}