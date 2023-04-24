package ru.andrewkir.hse_mooc.data.network.requests

data class LoginEmailRequest(
    val email: String,
    val password: String
)