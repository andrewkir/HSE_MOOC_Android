package ru.andrewkir.hse_mooc.network.responses.Login

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("accessToken")
    val access_token: String,
    @SerializedName("refreshToken")
    val refresh_token: String,
    val user: User
)