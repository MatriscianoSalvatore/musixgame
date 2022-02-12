package com.matrisciano.network.repository

import com.google.firebase.firestore.QuerySnapshot
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun getUsers(): Flow<Result<QuerySnapshot?>>
}