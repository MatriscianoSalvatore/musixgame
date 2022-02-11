package com.matrisciano.network.repository

import com.matrisciano.network.model.TrackID
import com.matrisciano.network.utils.Result

interface MusixmatchRepository {
    suspend fun getTrackID(artist: String, title: String): Result<TrackID>
}