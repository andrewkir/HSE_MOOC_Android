package ru.andrewkir.hse_mooc.domain.network.responses.Course


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Price(
    val amount: Double,
    val currency: String
) : Parcelable