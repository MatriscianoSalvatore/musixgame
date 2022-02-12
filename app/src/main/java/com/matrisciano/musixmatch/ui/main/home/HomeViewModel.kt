package com.matrisciano.musixmatch.ui.main.home

import androidx.lifecycle.ViewModel
import com.matrisciano.network.model.Lyrics
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.model.TopTracks
import com.matrisciano.network.model.TrackID
import com.matrisciano.network.repository.MusicRepository
import com.matrisciano.network.utils.Result

class HomeViewModel(private val musicRepository: MusicRepository) : ViewModel() {

    suspend fun getTrackID(artist: String, title: String): Result<TrackID> =
        musicRepository.getTrackID(artist, title)

    suspend fun getLyrics(trackID: String): Result<Lyrics> =
        musicRepository.getLyrics(trackID)

    suspend fun getTopTracks(): Result<TopTracks> =
        musicRepository.getTopTracks()

    suspend fun getSnippet(trackID: String): Result<Snippet> =
        musicRepository.getSnippet(trackID)
}