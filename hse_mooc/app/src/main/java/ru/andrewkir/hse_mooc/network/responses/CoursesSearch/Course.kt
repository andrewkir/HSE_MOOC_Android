package ru.andrewkir.hse_mooc.network.responses.CoursesSearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Course(
    val author: Author,
    val categories: List<Category>,
    val courseLanguages: List<String>,
    val courseName: String,
    val description: String,
    val duration: String,
    val id: String,
    val link: String,
    val previewImageLink: String,
    val price: Price,
    val rating: Rating,
    val shortDescription: String,
    val vendor: Vendor
): Parcelable