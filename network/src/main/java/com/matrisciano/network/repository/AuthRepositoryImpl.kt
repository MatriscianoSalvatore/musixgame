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
    override fun login(email: String, password: String): Flow<Result<FirebaseUser?>> =
        callbackFlow {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Firebase Authentication", ":success")
                        trySend(Result.success(it.result.user))
                    } else{
                        Log.w("Firebase Authentication", it.exception)
                        trySend(Result.error(it.exception?.localizedMessage ?: ""))
                    }
                }
            } catch (e: Exception) {
                trySend(Result.error(e.localizedMessage ?: ""))
            }
            awaitClose { channel.close() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun signup(
        name: String,
        email: String,
        password: String
    ): Flow<Result<FirebaseUser?>> = callbackFlow {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        Log.d("Firebase Authentication", "createUserWithEmail:success")
                        auth.currentUser?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                        )
                        trySend(Result.success(it.result.user))
                    } else {
                        Log.w("Firebase Authentication", it.exception)
                        trySend(Result.error(it.exception?.message ?: "createUserWithEmail:failure")
                        )
                    }
                }
        } catch (e: Exception) {
            trySend(Result.error(e.localizedMessage ?: "createUserWithEmail:failure"))
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(): Flow<Result<FirebaseUser?>> = callbackFlow {
        try {
            trySend(Result.success(auth.currentUser))
        } catch (e: Exception) {
            trySend(Result.error(e.localizedMessage ?: ""))
        }
        awaitClose { channel.close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserID(): Flow<Result<String?>> = callbackFlow {
        try {
            trySend(Result.success(auth.uid))
        } catch (e: Exception) {
            trySend(Result.error(e.localizedMessage ?: ""))
        }
        awaitClose { channel.close() }
    }
}