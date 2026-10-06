package com.jsalin.laguiadejerryapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jsalin.laguiadejerryapp.data.local.JerryGuideDatabase
import com.jsalin.laguiadejerryapp.data.local.entity.CharacterEntity
import com.jsalin.laguiadejerryapp.data.local.entity.RemoteKeyEntity
import com.jsalin.laguiadejerryapp.data.mapper.toDomain
import com.jsalin.laguiadejerryapp.data.mapper.toEntity
import com.jsalin.laguiadejerryapp.data.remote.api.CharacterApi
import com.jsalin.laguiadejerryapp.data.remote.safeApiCall
import com.jsalin.laguiadejerryapp.domain.model.DataError
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: CharacterApi,
    private val db: JerryGuideDatabase,
    private val now: () -> Long = System::currentTimeMillis,
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = db.characterDao()
    private val remoteKeyDao = db.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = remoteKeyDao.get(LABEL)?.lastUpdated
            ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        return if (now() - lastUpdated < CACHE_TIMEOUT) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult {
        val remoteKey = remoteKeyDao.get(LABEL)

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> remoteKey?.nextPage
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val response = safeApiCall { api.getCharacters(page) }
            val nextPage = if (response.info.next != null) page + 1 else null
            val lastUpdated = if (loadType == LoadType.REFRESH) now() else remoteKey?.lastUpdated ?: now()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) characterDao.clearAll()
                remoteKeyDao.upsert(RemoteKeyEntity(LABEL, nextPage, lastUpdated))
                characterDao.upsertAll(response.results.map { it.toDomain().toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = nextPage == null)
        } catch (e: DataError) {
            MediatorResult.Error(e)
        }
    }

    private companion object {
        const val LABEL = "characters"
        val CACHE_TIMEOUT = 24.hours.inWholeMilliseconds
    }
}