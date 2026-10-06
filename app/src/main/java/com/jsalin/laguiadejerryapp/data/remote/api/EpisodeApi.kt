package com.jsalin.laguiadejerryapp.data.remote.api

import com.jsalin.laguiadejerryapp.data.remote.dto.EpisodeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApi {

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): EpisodeDto

    @GET("episode/{ids}")
    suspend fun getEpisodes(@Path("ids") ids: String): List<EpisodeDto>
}