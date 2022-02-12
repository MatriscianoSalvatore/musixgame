package com.matrisciano.musixmatch.ui.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.matrisciano.network.repository.FirestoreRepository
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

class SigninViewModel(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    suspend fun getUsers(): Flow<Result<QuerySnapshot?>> =
        firestoreRepository.getUsers()
}