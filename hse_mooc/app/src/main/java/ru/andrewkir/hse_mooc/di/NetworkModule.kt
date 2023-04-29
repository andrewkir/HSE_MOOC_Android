package ru.andrewkir.hse_mooc.di

import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.andrewkir.hse_mooc.BuildConfig
import ru.andrewkir.hse_mooc.data.network.api.AuthApi
import ru.andrewkir.hse_mooc.domain.network.JWTAuthenticator
import ru.andrewkir.hse_mooc.domain.network.api.CoursesApi
import ru.andrewkir.hse_mooc.domain.network.api.TokensApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
    @Singleton
    fun provideAuthenticator(authenticator: JWTAuthenticator): Authenticator =
        authenticator

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authenticator: JWTAuthenticator? = null,
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

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCoursesApi(retrofit: Retrofit): CoursesApi = retrofit.create(CoursesApi::class.java)

    @Provides
    @Singleton
    fun provideTokensApi(retrofit: Retrofit): TokensApi = retrofit.create(TokensApi::class.java)




    companion object {
        const val BASE_URL = "https://api.mooc.ij.je/"
    }
}