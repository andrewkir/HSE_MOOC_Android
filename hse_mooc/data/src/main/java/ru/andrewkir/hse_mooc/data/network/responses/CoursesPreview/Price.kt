package ru.andrewkir.hse_mooc.data.network.responses.CoursesPreview


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Price(
    val amount: Double,
    val currency: String
) : Parcelable