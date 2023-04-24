package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.api.AuthApi
import ru.andrewkir.hse_mooc.data.network.requests.LoginEmailRequest
import ru.andrewkir.hse_mooc.data.network.requests.LoginUsernameRequest
import ru.andrewkir.hse_mooc.data.network.requests.RegisterRequest

class AuthRepository(
    private val api: AuthApi
) : BaseRepository() {

    suspend fun loginByEmail(
        email: String,
        password: String
    ) = protectedApiCall {
        api.loginByEmail(
            LoginEmailRequest(
                email,
                password
            )
        )
    }

    suspend fun loginByUsername(
        username: String,
        password: String
    ) = protectedApiCall {
        api.loginByUsername(
            LoginUsernameRequest(
                username,
                password
            )
        )
    }

    suspend fun register(
        email: String,
        username: String,
        password: String
    ) = protectedApiCall {
        api.register(
            RegisterRequest(
                email,
                username,
                password
            )
        )
    }
}