package com.jsalin.laguiadejerryapp.domain.repository

import androidx.paging.PagingData
import com.jsalin.laguiadejerryapp.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<PagingData<Character>>
    suspend fun getCharacter(id: Int): Character
    fun searchCharacters(query: String): Flow<PagingData<Character>>
}