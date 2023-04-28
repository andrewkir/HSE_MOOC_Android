package ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Category(
    val id: Int,
    val name: Name
) : Parcelable