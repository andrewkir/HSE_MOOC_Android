package ru.andrewkir.hse_mooc.network.responses.CoursesSearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class External(
    val averageScore: Double,
    val countReviews: Int
): Parcelable