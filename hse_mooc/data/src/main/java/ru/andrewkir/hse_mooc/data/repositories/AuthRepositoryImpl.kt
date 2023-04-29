package ru.andrewkir.hse_mooc.data.repositories

import ru.andrewkir.hse_mooc.data.network.api.AuthApi
import ru.andrewkir.hse_mooc.data.network.requests.LoginEmailRequest
import ru.andrewkir.hse_mooc.data.network.requests.LoginUsernameRequest
import ru.andrewkir.hse_mooc.data.network.requests.RegisterRequest
import ru.andrewkir.hse_mooc.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository, BaseRepository() {

    override suspend fun loginByEmail(
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

    override suspend fun loginByUsername(
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

    override suspend fun register(
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