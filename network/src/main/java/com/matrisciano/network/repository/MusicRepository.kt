package com.matrisciano.network.repository

import com.matrisciano.network.model.Lyrics
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.model.TopTracks
import com.matrisciano.network.model.TrackID
import com.matrisciano.network.utils.Result

interface MusicRepository {
    suspend fun getTrackID(artist: String, title: String): Result<TrackID>
    suspend fun getLyrics(trackID: String): Result<Lyrics>
    suspend fun getTopTracks(): Result<TopTracks>
    suspend fun getSnippet(trackID: String): Result<Snippet>
}