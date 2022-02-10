package com.matrisciano.musixmatch


import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

class Api {


    interface GetTrack {
        @Headers("apikey: " + "276b2392f053c47db5b3b5f072f54aa7")
        @GET("track.search")
        fun getTrackIDData(
            @Query("q_artist") q_artist: String,
            @Query("q_track") q_track: String,
            @Query("page_size") page_size: Number,
            @Query("s_track_rating") s_track_rating: String,
            @Query("s_artist_rating") s_artist_rating: String,
            @Query("apikey") apikey: String
        ): Call<TrackIDResponse>
    }

    class TrackIDResponse {
        @SerializedName("message")
        var message: TrackIDMessage? = null
    }

    class TrackIDMessage {
        @SerializedName("body")
        var body: TrackIDBody? = null
    }

    class TrackIDBody {
        @SerializedName("track_list")
        var track_list: List<TrackIDTrackList>? = null
    }

    class TrackIDTrackList {
        @SerializedName("track")
        var track: TrackIDTrack? = null
    }

    class TrackIDTrack {
        @SerializedName("track_id")
        var track_id: Object? = null
    }






    interface GetLyrics {
        @Headers("apikey: " + "276b2392f053c47db5b3b5f072f54aa7")
        @GET("track.lyrics.get")
        fun getCurrentTrackData(
            @Query("track_id") track_id: String,
            @Query("apikey") apikey: String
        ): Call<TrackResponse>
    }

    class TrackResponse {
        @SerializedName("message")
        var message: Message? = null
    }

    class Message {
        @SerializedName("body")
        var body: Body? = null
    }

    class Body {
        @SerializedName("lyrics")
        var lyrics: Lyrics? = null
    }

    class Lyrics {
        @SerializedName("lyrics_body")
        var lyrics_body: String? = null
    }




    interface GetTopTracks {
        @Headers("apikey: " + "276b2392f053c47db5b3b5f072f54aa7")
        @GET("chart.tracks.get")
        fun getTopTracks(
            @Query("chart_name") chart_name: String,
            @Query("page") page: Number,
            @Query("page_size") page_size: Number,
            @Query("country") country: String,
            @Query("f_has_lyrics") f_has_lyrics: Number,
            @Query("apikey") apikey: String
        ): Call<TopTracksResponse>
    }

    class TopTracksResponse {
        @SerializedName("message")
        var message: TopTracksMessage? = null
    }

    class TopTracksMessage {
        @SerializedName("body")
        var body: TopTracksBody? = null
    }

    class TopTracksBody {
        @SerializedName("track_list")
        var track_list: List<TopTracksTrackList>? = null
    }

    class TopTracksTrackList {
        @SerializedName("track")
        var track: TopTracksTrack? = null
    }

    class TopTracksTrack {
        @SerializedName("track_id")
        var track_id: Object? = null
        var artist_name: Object? = null
    }




    interface GetSnippet {
        @Headers("apikey: " + "276b2392f053c47db5b3b5f072f54aa7")
        @GET("track.snippet.get")
        fun getCurrentTrackData(
            @Query("track_id") track_id: String,
            @Query("apikey") apikey: String
        ): Call<SnippetResponse>
    }

    class SnippetResponse {
        @SerializedName("message")
        var message: SnippetMessage? = null
    }

    class SnippetMessage {
        @SerializedName("body")
        var body: SnippetBody? = null
    }

    class SnippetBody {
        @SerializedName("snippet")
        var snippet: SnippetLyrics? = null
    }

    class SnippetLyrics {
        @SerializedName("snippet_body")
        var snippet_body: String? = null
    }


}