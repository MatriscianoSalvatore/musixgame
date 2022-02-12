package com.matrisciano.musixmatch.ui.whosings

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.repository.FirestoreRepository
import com.matrisciano.network.repository.MusixmatchRepository
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.flow.Flow

class WhoSingsViewModel(private val musixmatchRepository: MusixmatchRepository, private val firestoreRepository: FirestoreRepository ) : ViewModel() {

    suspend fun getSnippet(trackID: String): Result<Snippet> =
        musixmatchRepository.getSnippet(trackID)

    suspend fun getUsers(): Flow<Result<QuerySnapshot?>> =
        firestoreRepository.getUsers()
}