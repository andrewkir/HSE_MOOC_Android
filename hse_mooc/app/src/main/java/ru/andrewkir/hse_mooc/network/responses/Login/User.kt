package ru.andrewkir.hse_mooc.network.responses.Login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val email: String,
    val username: String
) : Parcelable