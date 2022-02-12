package com.matrisciano.network.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRepositoryImpl(val firestore: FirebaseFirestore): FirestoreRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUsers() = callbackFlow {
        val collection = firestore.collection("users")
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                Result.success(value)
            } else {
                Result.error(error.message!!)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }
}