package ru.andrewkir.hse_mooc.data.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Category(
    val id: Int,
    val name: Name
) : Parcelable