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
    override fun createUser(userID: String, email: String): Result<Boolean> {

        var result = Result.success(true)
        val user = hashMapOf(
            "email" to email,
            "points" to 0,
        )

        firestore.collection(COLLECTION_USER).document(userID).set(user)
            .addOnSuccessListener {
                Log.d("UserRepository", "DocumentSnapshot added with ID: $userID")
            }
            .addOnFailureListener { e ->
                Log.w("UserRepository", "Error adding document", e)
                if (e.message != null)
                    result = Result.error(e.message!!)
                else
                    result = Result.error("Generic error")
            }
        return result
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun addPoints(userID: String, points: Long): Result<Boolean>  {

        var result = Result.success(true)
        firestore.collection(COLLECTION_USER).document(userID).get()
            .addOnSuccessListener { document ->

                firestore.collection(COLLECTION_USER).document(userID)
                    .update("points", document.data?.get("points") as Long + points)
                    .addOnSuccessListener {
                        Log.d("UserRepository", "DocumentSnapshot added with ID: $userID")
                    }
                    .addOnFailureListener { e ->
                        Log.w("UserRepository", "Error adding document", e)
                        if (e.message != null)
                            result = Result.error(e.message!!)
                        else
                            result = Result.error("Generic error")
                    }
            }
            .addOnFailureListener { e ->
                if (e.message != null)
                    result = Result.error(e.message!!)
                else
                    result = Result.error("Generic error")
            }
        return result
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(userID: String): Flow<Result<User>> = callbackFlow {
        firestore.collection(COLLECTION_USER).document(userID).get()
            .addOnSuccessListener { document ->
                try {
                    document?.toObject(User::class.java)?.let {
                        trySend(Result.success(it))
                    }
                } catch (e: Exception) {
                    trySend(Result.error(e.localizedMessage ?: ""))
                    Log.e("UserRepository", "Error getting documents. ${e.localizedMessage}", e)
                }
            }
            .addOnFailureListener {
                trySend(Result.error(it.localizedMessage ?: ""))
                Log.e("UserRepository", "Error getting documents. ${it.localizedMessage}", it)
            }

        awaitClose { channel.close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllUsers(): Flow<Result<List<User>>> = callbackFlow {
        firestore.collection(COLLECTION_USER).get()
            .addOnSuccessListener { collection ->
                try {
                    collection?.toObjects(User::class.java)?.let {
                        trySend(Result.success(it))
                    }
                } catch (e: Exception) {
                    trySend(Result.error(e.localizedMessage ?: ""))
                    Log.e("UserRepository", "Error getting documents. ${e.localizedMessage}", e)
                }
            }
            .addOnFailureListener {
                trySend(Result.error(it.localizedMessage ?: ""))
                Log.e("UserRepository", "Error getting documents. ${it.localizedMessage}", it)
            }

        awaitClose { channel.close() }
    }
}