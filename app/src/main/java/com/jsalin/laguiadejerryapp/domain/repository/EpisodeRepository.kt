package com.jsalin.laguiadejerryapp.domain.repository

import com.jsalin.laguiadejerryapp.domain.model.Episode

interface EpisodeRepository {
    suspend fun getEpisodes(ids: List<Int>): List<Episode>
}