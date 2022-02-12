package com.matrisciano.network.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val email: String? = null, val points: Long? = 0)
