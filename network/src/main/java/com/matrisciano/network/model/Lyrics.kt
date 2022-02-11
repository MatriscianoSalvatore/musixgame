package com.matrisciano.network.model

import kotlinx.serialization.*

@Serializable
data class Lyrics (
    val message: LyricsMessage? = null
)

@Serializable
data class LyricsMessage (
    val body: LyricsBody? = null
)

@Serializable
data class LyricsBody (
    val lyrics: LyricsClass? = null
)

@Serializable
data class LyricsClass (
    @SerialName("lyrics_id")
    val lyricsID: Long? = null,

    val explicit: Long? = null,

    @SerialName("lyrics_body")
    val lyricsBody: String? = null,

    @SerialName("script_tracking_url")
    val scriptTrackingURL: String? = null,

    @SerialName("pixel_tracking_url")
    val pixelTrackingURL: String? = null,

    @SerialName("lyrics_copyright")
    val lyricsCopyright: String? = null,

    @SerialName("updated_time")
    val updatedTime: String? = null
)


