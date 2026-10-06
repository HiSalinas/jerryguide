package com.jsalin.laguiadejerryapp.data.mapper

import com.jsalin.laguiadejerryapp.data.local.entity.EpisodeEntity
import com.jsalin.laguiadejerryapp.data.remote.dto.EpisodeDto
import com.jsalin.laguiadejerryapp.domain.model.Episode

fun EpisodeDto.toDomain() = Episode(
    id = id,
    name = name,
    airDate = airDate,
    code = episode,
)

fun Episode.toEntity() = EpisodeEntity(id = id, name = name, airDate = airDate, code = code)

fun EpisodeEntity.toDomain() = Episode(id = id, name = name, airDate = airDate, code = code)