package ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class CoursesPreviewResponse(
    val countPages: Int,
    val courses: List<CoursePreview>,
    val nextPage: String
) : Parcelable