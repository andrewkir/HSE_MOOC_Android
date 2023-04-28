package ru.andrewkir.hse_mooc.domain.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Vendor(
    val icon: String,
    val id: String,
    val link: String,
    val name: String
) : Parcelable