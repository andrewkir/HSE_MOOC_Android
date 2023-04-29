package ru.andrewkir.hse_mooc.data.network.api

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import ru.andrewkir.hse_mooc.data.network.requests.LoginEmailRequest
import ru.andrewkir.hse_mooc.data.network.requests.LoginUsernameRequest
import ru.andrewkir.hse_mooc.data.network.requests.RegisterRequest
import ru.andrewkir.hse_mooc.domain.network.responses.Login.LoginResponse

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