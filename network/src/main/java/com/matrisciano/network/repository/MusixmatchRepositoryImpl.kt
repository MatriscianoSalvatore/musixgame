package com.matrisciano.network.repository

import com.matrisciano.network.model.Lyrics
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.model.TopTracks
import com.matrisciano.network.model.TrackID
import com.matrisciano.network.service.MusixmatchService
import com.matrisciano.network.utils.ResponseHandler
import com.matrisciano.network.utils.Result

class MusixmatchRepositoryImpl(private var musixmatchService: MusixmatchService, private var responseHandler: ResponseHandler): MusixmatchRepository {

    override suspend fun getTrackID(artist: String, title: String): Result<TrackID> = responseHandler.getData {
        musixmatchService.getTrackID(artist, title, 1, "desc", "desc")
    }

    override suspend fun getLyrics(trackID: String): Result<Lyrics> = responseHandler.getData {
        musixmatchService.getLyrics(trackID)
    }

    override suspend fun getTopTracks(): Result<TopTracks> = responseHandler.getData {
        musixmatchService.getTopTracks("top",1, 45, "it", 1)
    }

    override suspend fun getSnippet(trackID: String): Result<Snippet> = responseHandler.getData {
        musixmatchService.getSnippet(trackID)
    }
}