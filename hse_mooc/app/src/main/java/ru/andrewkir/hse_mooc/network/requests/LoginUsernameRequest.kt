package ru.andrewkir.hse_mooc.network.requests

data class LoginUsernameRequest(
    val username: String,
    val password: String
)