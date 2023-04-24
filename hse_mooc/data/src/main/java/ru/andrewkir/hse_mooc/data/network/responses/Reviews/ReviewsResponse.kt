package ru.andrewkir.hse_mooc.data.network.responses.Reviews


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class ReviewsResponse(
    val courseId: String,
    val reviews: List<Review>
) : Parcelable