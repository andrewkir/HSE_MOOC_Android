package ru.andrewkir.hse_mooc.domain.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.andrewkir.hse_mooc.domain.BuildConfig
import ru.andrewkir.hse_mooc.domain.network.api.TokensApi
import java.util.concurrent.TimeUnit

class ApiProvider {
    companion object {
        private const val BASE_URL = "https://api.mooc.ij.je/"
    }

    fun <Api> provideApi(
        api: Class<Api>,
        context: Context,
        accessToken: String? = null,
        refreshToken: String? = null
    ): Api {
        val authenticator =
            ru.andrewkir.hse_mooc.domain.network.JWTAuthenticator(context, provideTokensApi())
        return Retrofit.Builder()
            .baseUrl(ru.andrewkir.hse_mooc.domain.network.ApiProvider.Companion.BASE_URL)
            .client(provideOkHTPPClient(authenticator, accessToken, refreshToken))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun provideTokensApi(): TokensApi {
        return Retrofit.Builder()
            .baseUrl(ru.andrewkir.hse_mooc.domain.network.ApiProvider.Companion.BASE_URL)
            .client(provideOkHTPPClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokensApi::class.java)
    }

    private fun provideOkHTPPClient(
        authenticator: ru.andrewkir.hse_mooc.domain.network.JWTAuthenticator? = null,
        accessToken: String? = null,
        refreshToken: String? = null
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().also {
                        it.addHeader("Accept", "application/json")
                        if (accessToken != null) {
                            it.addHeader(
                                "Authorization",
                                "Bearer $accessToken"
                            )
                        }
                        if (refreshToken != null) {
                            it.addHeader(
                                "x-refresh-token",
                                "$refreshToken"
                            )
                        }
                    }.build()
                )
            }
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
                if (authenticator != null) {
                    client.authenticator(authenticator)
                }
            }.build()
    }
}