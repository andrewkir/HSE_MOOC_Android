package ru.andrewkir.hse_mooc.data.network.requests

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)