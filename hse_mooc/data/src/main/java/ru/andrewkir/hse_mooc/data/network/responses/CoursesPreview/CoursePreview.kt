package ru.andrewkir.hse_mooc.data.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class CoursePreview(
    val author: Author,
    val categories: List<Category>,
    val countViews: Int,
    val courseLanguages: List<String>,
    val courseName: String,
    val duration: String,
    val id: String,
    val link: String,
    val previewImageLink: String,
    val price: Price,
    val rating: Rating,
    val shortDescription: String,
    val vendor: Vendor
) : Parcelable