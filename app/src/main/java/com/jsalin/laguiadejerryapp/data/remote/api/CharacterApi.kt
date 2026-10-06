package com.jsalin.laguiadejerryapp.data.remote.api

import com.jsalin.laguiadejerryapp.data.remote.dto.CharacterDto
import com.jsalin.laguiadejerryapp.data.remote.dto.PageResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
    ): PageResponseDto<CharacterDto>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}