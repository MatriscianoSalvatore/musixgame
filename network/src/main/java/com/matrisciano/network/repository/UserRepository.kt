package com.matrisciano.network.repository

import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun createUser(userID: String, email: String): Flow<Result<Boolean>>
    fun addPoints(userID: String, points: Long): Flow<Result<Boolean>>
    fun getUser(userID: String): Flow<Result<User>>
    fun getAllUsers(): Flow<Result<List<User>>>
}