package ru.andrewkir.hse_mooc.domain.responses.Compilations


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Name(
    val en: String,
    val ru: String
) : Parcelable