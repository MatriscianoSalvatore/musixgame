package com.matrisciano.network.repository

import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /** Get User collection from Firestore */
    fun getUser(id: String): Flow<Result<User>>
}
