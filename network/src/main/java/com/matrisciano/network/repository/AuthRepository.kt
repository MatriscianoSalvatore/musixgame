package com.matrisciano.network.repository

import com.google.firebase.auth.FirebaseUser
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<FirebaseUser?>>
    fun signup(name: String, email: String, password: String): Flow<Result<FirebaseUser?>>
    fun logout(): Flow<Result<Boolean>>
    fun getUser(): Flow<Result<FirebaseUser?>>
    fun getUserID(): Flow<Result<String?>>
}