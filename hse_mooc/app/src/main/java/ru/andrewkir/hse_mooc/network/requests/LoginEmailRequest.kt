package ru.andrewkir.hse_mooc.network.requests

data class LoginEmailRequest(
    val email: String,
    val password: String
)