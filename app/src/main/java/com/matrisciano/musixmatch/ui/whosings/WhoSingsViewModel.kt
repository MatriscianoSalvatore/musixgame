package com.matrisciano.musixmatch.ui.whosings

import androidx.lifecycle.ViewModel
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.repository.MusicRepository
import com.matrisciano.network.utils.Result

class WhoSingsViewModel(private val musicRepository: MusicRepository) : ViewModel() {

    suspend fun getSnippet(trackID: String): Result<Snippet> =
        musicRepository.getSnippet(trackID)
}