package ru.andrewkir.hse_mooc.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Internal(
    val averageScore: Double,
    val countReviews: Int
) : Parcelable