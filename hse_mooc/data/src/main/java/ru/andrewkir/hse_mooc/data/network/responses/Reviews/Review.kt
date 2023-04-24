package ru.andrewkir.hse_mooc.data.network.responses.Reviews


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Review(
    val creationDate: String,
    val id: String,
    val rating: Double,
    val text: String,
    var isMyReview: Boolean?,
    val user: User
) : Parcelable