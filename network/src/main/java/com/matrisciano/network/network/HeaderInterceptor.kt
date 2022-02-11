package com.matrisciano.network.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("apikey", "YOUR_MUSIXMATCH_API_KEY")
        return chain.proceed(builder.build())
    }
}