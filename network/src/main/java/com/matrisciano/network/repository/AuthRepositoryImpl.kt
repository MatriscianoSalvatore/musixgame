package com.matrisciano.network.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun login(email: String, password: String): Flow<Result<Boolean>> = callbackFlow {
        try {
            auth.signInWithEmailAndPassword(email, password)
            Log.d("Firebase Authentication", ":success")
            trySend(Result.success(true))
        } catch (e: Exception) {
            trySend(Result.success(true))
        }
        awaitClose { channel.close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun signup(name: String, email: String, password: String): Flow<Result<Boolean>> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase Authentication", "createUserWithEmail:success")
                    auth.currentUser?.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                    )
                    trySend(Result.success(true))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Firebase Authentication", "createUserWithEmail:failure", task.exception)
                    trySend(Result.error(task.exception?.message ?: "createUserWithEmail:failure"))
                }
            }
        awaitClose { channel.close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun logout(): Flow<Result<Boolean>> = callbackFlow {
        try {
            auth.signOut()
            trySend(Result.success(true))
        } catch (e: Exception) {
            trySend(Result.error(e.localizedMessage ?: ""))
        }
        awaitClose { channel.close() }
    }
}