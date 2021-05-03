package ru.andrewkir.hse_mooc.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import ru.andrewkir.hse_mooc.network.responses.Reviews.Review

@Parcelize
data class Course(
    val author: Author,
    val categories: List<Category>,
    val countViews: Int,
    val courseLanguages: List<String>,
    val courseName: String,
    val description: String,
    val duration: String,
    val id: String,
    val link: String,
    val previewImageLink: String,
    val price: Price,
    val rating: Rating,
    val reviews: List<Review>,
    val shortDescription: String,
    val vendor: Vendor
) : Parcelable