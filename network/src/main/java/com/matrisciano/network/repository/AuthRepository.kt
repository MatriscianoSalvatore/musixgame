package com.matrisciano.network.repository

import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<Boolean>>
    fun signup(name: String, email: String, password: String): Flow<Result<Boolean>>
    fun logout(): Flow<Result<Boolean>>
}