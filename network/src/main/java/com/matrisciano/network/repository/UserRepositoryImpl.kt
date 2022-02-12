package com.matrisciano.network.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepositoryImpl(private val firestore: FirebaseFirestore) : UserRepository {

    companion object {
        private const val COLLECTION_USER = "users"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(id: String): Flow<Result<User>> = callbackFlow {
        firestore.collection(COLLECTION_USER).document(id).get()
            .addOnSuccessListener { document ->
                try {
                    document?.toObject(User::class.java)?.let {
                        trySend(Result.success(it))
                    }
                } catch (e: Exception) {
                    trySend(Result.error(e.localizedMessage ?: ""))
                    Log.e("FIREBASE_TAG", "Error getting documents. ${e.localizedMessage}", e)
                }
            }
            .addOnFailureListener {
                trySend(Result.error(it.localizedMessage ?: ""))
                Log.e("FIREBASE_TAG", "Error getting documents. ${it.localizedMessage}", it)
            }

        awaitClose { channel.close() }
    }

}
