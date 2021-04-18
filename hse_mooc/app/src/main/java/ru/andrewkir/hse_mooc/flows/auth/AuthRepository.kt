package ru.andrewkir.hse_mooc.flows.auth

import kotlinx.coroutines.delay
import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.AuthApi
import ru.andrewkir.hse_mooc.network.requests.LoginEmailRequest
import ru.andrewkir.hse_mooc.network.requests.LoginUsernameRequest
import ru.andrewkir.hse_mooc.network.requests.RegisterRequest
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.network.responses.LoginResponse

class AuthRepository(
    private val api: AuthApi
) : BaseRepository() {

    suspend fun loginByEmail(
        email: String,
        password: String
    ) = protectedApiCall {
        api.loginByEmail(LoginEmailRequest(email, password))
    }

    suspend fun loginByUsername(
        username: String,
        password: String
    ) = protectedApiCall {
        api.loginByUsername(LoginUsernameRequest(username, password))
    }

    suspend fun register(
        email: String,
        username: String,
        password: String
    ) = protectedApiCall {
        api.register(RegisterRequest(email, username, password))
    }

//    suspend fun login(
//        username: String,
//        password: String
//    ): ApiResponse.OnSuccessResponse<LoginResponse> {
//        delay(500)
//        return ApiResponse.OnSuccessResponse(LoginResponse("sample access token", "sample refresh token"))
//    }

}