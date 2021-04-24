package ru.andrewkir.hse_mooc.network.responses

import com.google.gson.annotations.SerializedName

data class TokensResponse (
    @SerializedName("accessToken")
    val access_token: String,
    @SerializedName("refreshToken")
    val refresh_token: String
)