package ru.andrewkir.hse_mooc.data.network.responses.Reviews


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class User(
    val username: String
) : Parcelable