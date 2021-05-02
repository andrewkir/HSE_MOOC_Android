package ru.andrewkir.hse_mooc.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class External(
    val averageScore: Int,
    val countReviews: Int
) : Parcelable