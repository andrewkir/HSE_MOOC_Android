package ru.andrewkir.hse_mooc.domain.repositories

import okhttp3.ResponseBody
import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.responses.Login.LoginResponse

interface AuthRepository {
    suspend fun loginByEmail(email: String, password: String): ApiResponse<LoginResponse>
    suspend fun loginByUsername(username: String, password: String): ApiResponse<LoginResponse>
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): ApiResponse<ResponseBody>
}