package com.matrisciano.musixmatch.ui.main.home

import androidx.lifecycle.ViewModel
import com.matrisciano.network.model.Lyrics
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.model.TopTracks
import com.matrisciano.network.model.TrackID
import com.matrisciano.network.repository.MusixmatchRepository
import com.matrisciano.network.utils.Result

class HomeViewModel(private val musixmatchRepository: MusixmatchRepository) : ViewModel() {

    /*private var _trackID = MutableLiveData<TrackID>()
    val trackID: LiveData<TrackID> get() = _trackID

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error*/

    suspend fun getTrackID(artist: String, title: String): Result<TrackID> =
        musixmatchRepository.getTrackID(artist, title)

    suspend fun getLyrics(trackID: String): Result<Lyrics> =
        musixmatchRepository.getLyrics(trackID)

    suspend fun getTopTracks(): Result<TopTracks> =
        musixmatchRepository.getTopTracks()

    suspend fun getSnippet(trackID: String): Result<Snippet> =
        musixmatchRepository.getSnippet(trackID)
}