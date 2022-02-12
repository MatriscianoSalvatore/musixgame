package com.matrisciano.network.repository

import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<Result<List<User>>>
    fun getUser(userID: String): Flow<Result<User>>
}