package ru.andrewkir.hse_mooc.domain.network.responses.Compilations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompilationsResponseItem(
    val icon: String,
    val link: String,
    val name: Name
) : Parcelable