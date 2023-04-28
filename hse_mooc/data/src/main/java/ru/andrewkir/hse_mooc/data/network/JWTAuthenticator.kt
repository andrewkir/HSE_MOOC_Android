package ru.andrewkir.hse_mooc.domain.network

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.andrewkir.hse_mooc.domain.common.UserPrefsManager
import ru.andrewkir.hse_mooc.domain.model.ApiResponse
import ru.andrewkir.hse_mooc.domain.network.api.TokensApi
import ru.andrewkir.hse_mooc.domain.repositories.BaseRepository

class JWTAuthenticator(
    context: Context,
    private val tokensApi: TokensApi
) : Authenticator, BaseRepository() {

    private val prefsManager = UserPrefsManager(context)

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = prefsManager.refreshToken
            when (val tokensResponse =
                protectedApiCall { tokensApi.refreshAccessToken(refreshToken!!) }
            ) {
                is ApiResponse.OnSuccessResponse<*> -> {
                    prefsManager.accessToken = tokensResponse.value.access_token

                    prefsManager.refreshToken = tokensResponse.value.refresh_token

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${tokensResponse.value.access_token}")
                        .build()
                }
                else -> null
            }
        }
    }
}