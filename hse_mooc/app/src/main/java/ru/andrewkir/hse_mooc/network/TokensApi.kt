package ru.andrewkir.hse_mooc.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.andrewkir.hse_mooc.network.responses.LoginResponse
import ru.andrewkir.hse_mooc.network.responses.TokensResponse

interface TokensApi {

    @FormUrlEncoded
    @POST("auth/refresh")
    suspend fun refreshAccessToken(
        @Field("refresh_token") refresh_token: String
    ): TokensResponse
}