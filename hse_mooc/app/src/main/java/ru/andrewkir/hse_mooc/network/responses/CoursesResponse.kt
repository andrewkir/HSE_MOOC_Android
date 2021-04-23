package ru.andrewkir.hse_mooc.network.responses

data class CoursesResponse(
    val courses: List<Course>,
    val nextPage: String
)