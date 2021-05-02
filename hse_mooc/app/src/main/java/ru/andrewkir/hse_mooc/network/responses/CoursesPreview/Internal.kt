package ru.andrewkir.hse_mooc.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Internal(
    val averageScore: Int,
    val average_score: Int,
    val countReviews: Int,
    val count_reviews: Int
) : Parcelable