package ru.andrewkir.hse_mooc.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import ru.andrewkir.hse_mooc.network.api.AuthApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse

abstract class BaseRepository {

    suspend fun <T> protectedApiCall(
        api: suspend () -> T
    ): ApiResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                ApiResponse.OnSuccessResponse(api.invoke())
            } catch (ex: Throwable) {
                when (ex) {
                    is HttpException -> {
                        ApiResponse.OnErrorResponse(false, ex.code(), ex.response()?.errorBody())
                    }
                    else -> {
                        ApiResponse.OnErrorResponse(true, null, null)
                    }
                }
            }
        }
    }

    suspend fun userLogout(api: AuthApi): ApiResponse<ResponseBody> {
        return protectedApiCall { api.logout() }
    }
}