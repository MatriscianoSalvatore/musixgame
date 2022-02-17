package com.matrisciano.network.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("apikey", "276b2392f053c47db5b3b5f072f54aa7")
        return chain.proceed(builder.build())
    }
}