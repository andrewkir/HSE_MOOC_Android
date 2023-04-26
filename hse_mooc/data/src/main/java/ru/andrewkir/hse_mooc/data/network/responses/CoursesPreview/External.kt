package ru.andrewkir.hse_mooc.data.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class External(
    val averageScore: Double,
    val countReviews: Int
) : Parcelable