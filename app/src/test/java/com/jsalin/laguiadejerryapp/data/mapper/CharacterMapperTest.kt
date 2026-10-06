package com.jsalin.laguiadejerryapp.data.mapper

import com.jsalin.laguiadejerryapp.data.remote.dto.CharacterDto
import com.jsalin.laguiadejerryapp.data.remote.dto.LocationReferenceDto
import com.jsalin.laguiadejerryapp.domain.model.CharacterStatus
import org.junit.Assert.*
import org.junit.Test

class CharacterMapperTest {
    @Test
    fun `unexpected status maps to UNKNOWN`() {
        val character = characterDto(status = "Morty").toDomain()

        assertEquals(CharacterStatus.UNKNOWN, character.status)
    }

    @Test
    fun `episode urls map to ids and empty type maps to null`() {
        val character = characterDto(
            type = "",
            episode = listOf(
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/28",
            ),
        ).toDomain()

        assertEquals(listOf(1, 28), character.episodeIds)
        assertNull(character.type)
    }

    fun characterDto(
        id: Int = 1,
        name: String = "Rick Sanchez",
        status: String = "Alive",
        species: String = "Human",
        type: String = "",
        gender: String = "Male",
        origin: String = "Earth (C-137)",
        location: String = "Citadel of Ricks",
        episode: List<String> = listOf("https://rickandmortyapi.com/api/episode/1"),
    ) = CharacterDto(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationReferenceDto(origin, ""),
        location = LocationReferenceDto(location, ""),
        image = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
        episode = episode,
    )
}
