package ru.andrewkir.hse_mooc.network

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.andrewkir.hse_mooc.common.BaseRepository
import ru.andrewkir.hse_mooc.network.api.TokensApi
import ru.andrewkir.hse_mooc.network.responses.ApiResponse
import ru.andrewkir.hse_mooc.repository.UserPrefsManager

class JWTAuthenticator(
    context: Context,
    private val tokensApi: TokensApi
) : Authenticator, BaseRepository() {

    private val prefsManager = UserPrefsManager(context)

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = prefsManager.obtainRefreshToken()
            when (val tokensResponse =
                protectedApiCall { tokensApi.refreshAccessToken(refreshToken!!) }
                ) {
                is ApiResponse.OnSuccessResponse -> {

                    //TODO разобраться что возвращает refresh
                    prefsManager.saveAccessToken(
                        tokensResponse.value.access_token
                    )
                    prefsManager.saveRefreshToken(
                        tokensResponse.value.refresh_token
                    )

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${tokensResponse.value.access_token}")
                        .build()
                }
                else -> null
            }
        }
    }
}