package ru.andrewkir.hse_mooc.data.network.requests

data class LoginUsernameRequest(
    val username: String,
    val password: String
)