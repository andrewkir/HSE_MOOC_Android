package ru.andrewkir.hse_mooc.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.andrewkir.hse_mooc.BuildConfig
import ru.andrewkir.hse_mooc.network.api.TokensApi

class ApiProvider {
    companion object {
        private const val BASE_URL = "https://vk.com/"
    }

    fun <Api> provideApi(
        api: Class<Api>,
        context: Context,
        accessToken: String?,
        refreshToken: String?
    ): Api {
        val authenticator = JWTAuthenticator(context, provideTokensApi())
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHTPPClient(authenticator, accessToken, refreshToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun provideTokensApi(): TokensApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHTPPClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokensApi::class.java)
    }

    private fun provideOkHTPPClient(
        authenticator: JWTAuthenticator? = null,
        accessToken: String? = null,
        refreshToken: String? = null
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().also {
                        it.addHeader("Accept", "application/json")
                        if (accessToken != null)
                            it.addHeader("Authorization", "Bearer $accessToken")
                        //TODO к каждому запросу добавлять accessToken
                        if (refreshToken != null)
                            it.addHeader("x-refresh-token", "$refreshToken")
                    }.build()
                )
            }
            .also { client ->
                if (authenticator != null && refreshToken == null) {
                    client.authenticator(authenticator)
                }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build()
    }
}