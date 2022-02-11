package com.matrisciano.network.utils

sealed class Result<out R> {

    data class Success<out T>(
        val value: T
    ): Result<T>()

    data class Error<out T>(
        val message: String
    ): Result<T>()

    companion object {
        fun<T> success(value: T): Result<T> = Success(value)
        fun<T> error(message: String): Result<T> = Error(message)
    }
}