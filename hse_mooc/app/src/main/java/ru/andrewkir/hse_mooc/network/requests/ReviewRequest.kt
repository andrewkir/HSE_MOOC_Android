package ru.andrewkir.hse_mooc.network.requests

data class ReviewRequest(
    val rating: Double,
    val text: String
)