package com.matrisciano.network.model

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class TopTracks (
    val message: TopTracksMessage? = null
)

@Serializable
data class TopTracksMessage (
    val body: TopTracksBody? = null
)

@Serializable
data class TopTracksBody (
    @SerialName("track_list")
    val track_list: List<TopTracksList>? = null
)

@Serializable
data class TopTracksList (
    val track: TopTracksClass? = null
)

@Serializable
data class TopTracksClass (
    @SerialName("track_id")
    val track_id: Long? = null,

    @SerialName("track_name")
    val track_name: String? = null,

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
    val artist_name: String? = null,

    @SerialName("track_share_url")
    val trackShareURL: String? = null,

    @SerialName("track_edit_url")
    val trackEditURL: String? = null,

    val restricted: Long? = null,

    @SerialName("updated_time")
    val updatedTime: String? = null,

    @SerialName("primary_genres")
    val primaryGenres: TopTracksPrimaryGenres? = null
)

@Serializable
data class TopTracksPrimaryGenres (
    @SerialName("music_genre_list")
    val musicGenreList: List<MusicGenreList>? = null
)

@Serializable
data class MusicGenreList (
    @SerialName("music_genre")
    val musicGenre: MusicGenre? = null
)

@Serializable
data class MusicGenre (
    @SerialName("music_genre_id")
    val musicGenreID: Long? = null,

    @SerialName("music_genre_parent_id")
    val musicGenreParentID: Long? = null,

    @SerialName("music_genre_name")
    val musicGenreName: String? = null,

    @SerialName("music_genre_name_extended")
    val musicGenreNameExtended: String? = null,

    @SerialName("music_genre_vanity")
    val musicGenreVanity: String? = null
)