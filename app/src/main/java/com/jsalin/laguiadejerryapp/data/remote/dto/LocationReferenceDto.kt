package com.jsalin.laguiadejerryapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationReferenceDto(
    val name: String,
    val url: String
)