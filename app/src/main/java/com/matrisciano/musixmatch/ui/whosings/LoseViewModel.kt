package com.matrisciano.musixmatch.ui.whosings

import androidx.lifecycle.ViewModel
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.repository.MusixmatchRepository
import com.matrisciano.network.utils.Result

class LoseViewModel(private val repository: MusixmatchRepository) : ViewModel() {

    suspend fun getSnippet(trackID: String): Result<Snippet> =
        repository.getSnippet(trackID)
}