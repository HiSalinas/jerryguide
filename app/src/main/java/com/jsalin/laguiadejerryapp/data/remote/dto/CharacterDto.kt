package com.jsalin.laguiadejerryapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val episode: List<String>,
    val gender: String,
    val image: String,
    val location: LocationReferenceDto,
    val origin: LocationReferenceDto,
    val species: String,
    val type: String,
)