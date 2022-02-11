package com.matrisciano.network.service

import com.matrisciano.network.model.Lyrics
import com.matrisciano.network.model.Snippet
import com.matrisciano.network.model.TopTracks
import com.matrisciano.network.model.TrackID
import com.matrisciano.network.utils.Result
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusixmatchService {

    companion object {
        const val apiKey = "4ac3d61572388ffbcb08f9e160fec313"
    }

    @GET("track.search")
    suspend fun getTrackID(
        @Query("q_artist") q_artist: String,
        @Query("q_track") q_track: String,
        @Query("page_size") page_size: Int,
        @Query("s_track_rating") s_track_rating: String,
        @Query("s_artist_rating") s_artist_rating: String,
        @Query("apikey") apikey: String = apiKey
    ): Response<TrackID>

    @GET("track.lyrics.get")
    fun getLyrics(
        @Query("track_id") track_id: String,
        @Query("apikey") apikey: String = apiKey
    ): Response<Lyrics>

    @GET("chart.tracks.get")
    fun getTopTracks(
        @Query("chart_name") chart_name: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("country") country: String,
        @Query("f_has_lyrics") f_has_lyrics: Int,
        @Query("apikey") apikey: String = apiKey
    ): Response<TopTracks>

    @GET("track.snippet.get")
    fun getSnippet(
        @Query("track_id") track_id: String,
        @Query("apikey") apikey: String = apiKey
    ): Response<Snippet>
}