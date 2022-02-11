package com.matrisciano.network.model


import kotlinx.serialization.*

@Serializable
data class Snippet (
    val message: SnippetMessage? = null
)

@Serializable
data class SnippetMessage (
    val body: SnippetBody? = null
)

@Serializable
data class SnippetBody (
    val snippet: SnippetClass? = null
)

@Serializable
data class SnippetClass (
    @SerialName("snippet_id")
    val snippetID: Long? = null,

    @SerialName("snippet_language")
    val snippetLanguage: String? = null,

    val restricted: Long? = null,
    val instrumental: Long? = null,

    @SerialName("snippet_body")
    val snippet_body: String? = null,

    @SerialName("script_tracking_url")
    val scriptTrackingURL: String? = null,

    @SerialName("pixel_tracking_url")
    val pixelTrackingURL: String? = null,

    @SerialName("html_tracking_url")
    val htmlTrackingURL: String? = null,

    @SerialName("updated_time")
    val updatedTime: String? = null
)


