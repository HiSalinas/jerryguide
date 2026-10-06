package com.jsalin.laguiadejerryapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.jsalin.laguiadejerryapp.data.local.JerryGuideDatabase
import com.jsalin.laguiadejerryapp.data.mapper.toDomain
import com.jsalin.laguiadejerryapp.data.paging.CharacterRemoteMediator
import com.jsalin.laguiadejerryapp.data.paging.CharacterSearchPagingSource
import com.jsalin.laguiadejerryapp.data.remote.api.CharacterApi
import com.jsalin.laguiadejerryapp.data.remote.safeApiCall
import com.jsalin.laguiadejerryapp.domain.model.Character
import com.jsalin.laguiadejerryapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterApi,
    private val db: JerryGuideDatabase,
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = CharacterRemoteMediator(api, db),
        pagingSourceFactory = { db.characterDao().pagingSource() },
    ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override suspend fun getCharacter(id: Int): Character =
        db.characterDao().getById(id)?.toDomain()
            ?: safeApiCall { api.getCharacter(id) }.toDomain()

    override fun searchCharacters(query: String): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { CharacterSearchPagingSource(api, query) },
    ).flow

    private companion object {
        const val PAGE_SIZE = 20
    }
}