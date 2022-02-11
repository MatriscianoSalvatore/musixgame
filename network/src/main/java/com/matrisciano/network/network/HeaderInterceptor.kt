package com.matrisciano.network.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("apikey", "4ac3d61572388ffbcb08f9e160fec313")
        return chain.proceed(builder.build())
    }
}