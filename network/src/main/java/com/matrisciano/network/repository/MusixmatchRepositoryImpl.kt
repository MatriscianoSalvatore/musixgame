package com.matrisciano.network.repository

import com.matrisciano.network.model.TrackID
import com.matrisciano.network.service.MusixmatchService
import com.matrisciano.network.utils.ResponseHandler
import com.matrisciano.network.utils.Result

class MusixmatchRepositoryImpl(private var musixmatchService: MusixmatchService, private var responseHandler: ResponseHandler): MusixmatchRepository {
    override suspend fun getTrackID(artist: String, title: String): Result<TrackID> = responseHandler.getData {
        musixmatchService.getTrackID(artist, title, 1, "desc", "desc") //page size and rating are static for now
    }
}