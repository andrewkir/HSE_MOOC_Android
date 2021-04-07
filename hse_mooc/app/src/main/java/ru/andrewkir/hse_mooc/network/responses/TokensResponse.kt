package ru.andrewkir.hse_mooc.network.responses

data class TokensResponse (
    val access_token: String,
    val refresh_token: String
)