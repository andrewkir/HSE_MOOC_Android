package ru.andrewkir.hse_mooc.domain.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Author(
    val icon: String,
    val link: String,
    val name: String
) : Parcelable