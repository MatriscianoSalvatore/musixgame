package com.matrisciano.musixmatch.ui.whosings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.repository.MusicRepository
import com.matrisciano.network.repository.UserRepository
import com.matrisciano.network.utils.Result

class WhoSingsViewModel(
    private val musicRepository: MusicRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun getSnippet(trackID: String): Result<Snippet> =
        musicRepository.getSnippet(trackID)

    fun addPoints(userID: String, points: Long) =
        userRepository.addPoints(userID, points).asLiveData()
}