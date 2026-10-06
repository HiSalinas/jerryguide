package com.jsalin.laguiadejerryapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jsalin.laguiadejerryapp.data.mapper.toDomain
import com.jsalin.laguiadejerryapp.data.remote.api.CharacterApi
import com.jsalin.laguiadejerryapp.data.remote.safeApiCall
import com.jsalin.laguiadejerryapp.domain.model.DataError
import com.jsalin.laguiadejerryapp.domain.model.Character

class CharacterSearchPagingSource(
    private val api: CharacterApi,
    private val query: String,
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: 1
        return try {
            val response = safeApiCall { api.getCharacters(page = page, name = query) }
            LoadResult.Page(
                data = response.results.map { it.toDomain() },
                prevKey = null,
                nextKey = if (response.info.next != null) page + 1 else null,
            )
        } catch (e: DataError.NotFound) {
            LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        } catch (e: DataError) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = null
}