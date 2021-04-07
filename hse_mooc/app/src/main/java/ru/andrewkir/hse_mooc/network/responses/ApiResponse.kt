package ru.andrewkir.hse_mooc.network.responses

import okhttp3.ResponseBody

sealed class ApiResponse<out T> {
    data class OnSuccessResponse<T> (val value: T) : ApiResponse<T>()
    data class OnErrorResponse(
        val isNetworkFailure: Boolean,
        val code: Int?,
        val body: ResponseBody?
    ) : ApiResponse<Nothing>()
}