package ru.andrewkir.hse_mooc.network.requests

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)