package ru.andrewkir.hse_mooc.network.responses.CoursesSearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vendor (
    val id: String,
    val name: String,
    val icon: String,
    val link: String
): Parcelable