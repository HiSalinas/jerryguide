package com.jsalin.laguiadejerryapp.data.repository

import com.jsalin.laguiadejerryapp.data.local.dao.EpisodeDao
import com.jsalin.laguiadejerryapp.data.mapper.toDomain
import com.jsalin.laguiadejerryapp.data.mapper.toEntity
import com.jsalin.laguiadejerryapp.data.remote.api.EpisodeApi
import com.jsalin.laguiadejerryapp.data.remote.dto.EpisodeDto
import com.jsalin.laguiadejerryapp.data.remote.safeApiCall
import com.jsalin.laguiadejerryapp.domain.model.Episode
import com.jsalin.laguiadejerryapp.domain.repository.EpisodeRepository
import javax.inject.Inject
import kotlin.collections.map

class EpisodeRepositoryImpl @Inject constructor(
    private val api: EpisodeApi,
    private val episodeDao: EpisodeDao,
) : EpisodeRepository {

    override suspend fun getEpisodes(ids: List<Int>): List<Episode> {
        val cachedIds = episodeDao.getByIds(ids).map { it.id }.toSet()
        val missingIds = ids.filterNot { it in cachedIds }

        if (missingIds.isNotEmpty()) {
            val fetched = safeApiCall { fetchEpisodes(missingIds) }
            episodeDao.upsertAll(fetched.map { it.toDomain().toEntity() })
        }

        return episodeDao.getByIds(ids).map { it.toDomain() }
    }

    private suspend fun fetchEpisodes(ids: List<Int>): List<EpisodeDto> = when (ids.size) {
        1 -> listOf(api.getEpisode(ids.single()))
        else -> api.getEpisodes(ids.joinToString(","))
    }
}