package ru.andrewkir.hse_mooc.data.network.requests

data class ReviewRequest(
    val rating: Double,
    val text: String
)