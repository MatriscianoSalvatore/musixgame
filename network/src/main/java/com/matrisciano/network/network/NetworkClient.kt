package com.matrisciano.network.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {
    private var loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var headerInterceptor = HeaderInterceptor()

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.musixmatch.com/ws/1.1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}