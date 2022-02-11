package com.matrisciano.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class TrackID (
    val message: TrackIDMessage? = null
)

@Serializable
data class TrackIDMessage (
    val body: TrackIDBody? = null
)

@Serializable
data class TrackIDBody (
    @SerialName("track_list")
    val track_list: List<TrackList>? = null
)

@Serializable
data class TrackList (
    val track: Track? = null
)

@Serializable
data class Track (
    @SerialName("track_id")
    val track_id: Long? = null,

    @SerialName("track_name")
    val trackName: String? = null,

    @SerialName("track_name_translation_list")
    val trackNameTranslationList: JsonArray? = null,

    @SerialName("track_rating")
    val trackRating: Long? = null,

    @SerialName("commontrack_id")
    val commontrackID: Long? = null,

    val instrumental: Long? = null,
    val explicit: Long? = null,

    @SerialName("has_lyrics")
    val hasLyrics: Long? = null,

    @SerialName("has_subtitles")
    val hasSubtitles: Long? = null,

    @SerialName("has_richsync")
    val hasRichsync: Long? = null,

    @SerialName("num_favourite")
    val numFavourite: Long? = null,

    @SerialName("album_id")
    val albumID: Long? = null,

    @SerialName("album_name")
    val albumName: String? = null,

    @SerialName("artist_id")
    val artistID: Long? = null,

    @SerialName("artist_name")
    val artistName: String? = null,

    @SerialName("track_share_url")
    val trackShareURL: String? = null,

    @SerialName("track_edit_url")
    val trackEditURL: String? = null,

    val restricted: Long? = null,

    @SerialName("updated_time")
    val updatedTime: String? = null,

    @SerialName("primary_genres")
    val primaryGenres: PrimaryGenres? = null
)

@Serializable
data class PrimaryGenres (
    @SerialName("music_genre_list")
    val musicGenreList: JsonArray? = null
)