package ru.andrewkir.hse_mooc.network.responses.Compilations


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class CompilationsResponseItem(
    val icon: String,
    val link: String,
    val name: Name
) : Parcelable