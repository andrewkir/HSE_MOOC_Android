package ru.andrewkir.hse_mooc.domain.network.api

import retrofit2.http.Header
import retrofit2.http.POST
import ru.andrewkir.hse_mooc.data.network.responses.TokensResponse

interface TokensApi {

    @POST("/auth/refresh-token")
    suspend fun refreshAccessToken(
        @Header("x-refresh-token") refresh_token: String
    ): TokensResponse
}