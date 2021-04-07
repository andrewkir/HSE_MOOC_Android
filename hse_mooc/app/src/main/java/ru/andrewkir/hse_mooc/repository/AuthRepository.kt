package ru.andrewkir.hse_mooc.repository

import kotlinx.coroutines.delay
import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.AuthApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

class AuthRepository(
    private val api: AuthApi
) : BaseRepository() {

//    suspend fun login(
//        username: String,
//        password: String
//    ) = protectedApiCall {
//        api.login(username, password)
//    }

    suspend fun login(
        username: String,
        password: String
    ): ApiResponse.OnSuccessResponse<LoginResponse> {
        delay(500)
        return ApiResponse.OnSuccessResponse(LoginResponse("sample access token", "sample refresh token"))
    }

}