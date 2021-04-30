package ru.andrewkir.hse_mooc.network.responses.CoursesSearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: Name
) : Parcelable