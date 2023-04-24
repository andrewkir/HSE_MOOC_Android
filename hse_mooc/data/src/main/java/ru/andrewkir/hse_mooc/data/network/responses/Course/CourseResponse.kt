package ru.andrewkir.hse_mooc.data.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class CourseResponse(
    val course: Course,
    val isFavourite: Boolean,
    val isViewed: Boolean
) : Parcelable