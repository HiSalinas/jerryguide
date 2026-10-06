package com.jsalin.laguiadejerryapp.data.mapper

import com.jsalin.laguiadejerryapp.data.local.entity.CharacterEntity
import com.jsalin.laguiadejerryapp.data.remote.dto.CharacterDto
import com.jsalin.laguiadejerryapp.domain.model.Character
import com.jsalin.laguiadejerryapp.domain.model.CharacterGender
import com.jsalin.laguiadejerryapp.domain.model.CharacterStatus

fun CharacterDto.toDomain() = Character(
    id = id,
    name = name,
    status = status.toCharacterStatus(),
    species = species,
    type = type.ifBlank { null },
    gender = gender.toCharacterGender(),
    origin = origin.name.toKnownOrNull(),
    location = location.name.toKnownOrNull(),
    imageUrl = image,
    episodeIds = episode.mapNotNull { it.substringAfterLast('/').toIntOrNull() },
)

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin,
    location = location,
    imageUrl = imageUrl,
    episodeIds = episodeIds,
)

fun CharacterEntity.toDomain() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin,
    location = location,
    imageUrl = imageUrl,
    episodeIds = episodeIds,
)

private fun String.toCharacterStatus() = when (lowercase()) {
    "alive" -> CharacterStatus.ALIVE
    "dead" -> CharacterStatus.DEAD
    else -> CharacterStatus.UNKNOWN
}

private fun String.toCharacterGender() = when (lowercase()) {
    "female" -> CharacterGender.FEMALE
    "male" -> CharacterGender.MALE
    "genderless" -> CharacterGender.GENDERLESS
    else -> CharacterGender.UNKNOWN
}

private fun String.toKnownOrNull() = takeUnless { it.equals("unknown", ignoreCase = true) }

