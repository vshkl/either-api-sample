package com.vshkl.either

import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    /**
     * Base URL for the API. Since the sample is expected to be used with the mock server, we are
     * using localhost as the base URL. The 10.0.2.2 is the IP address through which the Android
     * emulator can access the localhost of the host machine.
     */
    private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"

    /**
     * Create [OkHttpClient] with [HttpLoggingInterceptor] for logging in a lazy manner.
     */
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY else
                        HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()
    }

    /**
     * Create [Retrofit] instance in a lazy manner. Uses [GsonConverterFactory] for converting JSON
     * response to Kotlin objects and [EitherCallAdapterFactory] for converting Retrofit's success
     * and error responses to respective success and error models.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(EitherCallAdapterFactory.create())
            .build()
    }

    /**
     * Create [Api] instance in a lazy manner.
     */
    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }
}
