package ru.andrewkir.hse_mooc.network.responses

data class Course(
    val author: Author,
    val courseLanguages: List<String>,
    val courseName: String,
    val description: String,
    val duration: String,
    val id: String,
    val link: String,
    val previewImageLink: String,
    val price: String,
    val shortDescription: String,
    val vendor: String
)