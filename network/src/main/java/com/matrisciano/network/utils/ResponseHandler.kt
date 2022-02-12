package com.matrisciano.network.utils

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ResponseHandler {
    suspend fun <T> getData(call: suspend () -> Response<T>): Result<T> {
        val response = call()
        return try {
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body)
            } else Result.error<String>(response.code().httpErrorCode())
            Result.error("Generic Error")
        } catch (exception: Exception) {
            Result.error(exception.toCustomException())
        }
    }
}

fun Exception.toCustomException(): String = when (this) {
    is IOException -> "IOException"
    is HttpException -> this.code().httpErrorCode()
    is SocketTimeoutException -> "SocketTimeoutException"
    else -> "Generic Error"
}

fun Int.httpErrorCode(): String = when (this) {
    401 -> "401 error"
    404 -> "404 error"
    else -> "Generic Error"
}