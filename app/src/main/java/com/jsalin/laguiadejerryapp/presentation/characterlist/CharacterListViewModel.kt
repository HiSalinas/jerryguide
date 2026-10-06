package com.jsalin.laguiadejerryapp.presentation.characterlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jsalin.laguiadejerryapp.domain.model.Character
import com.jsalin.laguiadejerryapp.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var query by mutableStateOf(savedStateHandle.get<String>(QUERY_KEY).orEmpty())
        private set

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val characters: Flow<PagingData<Character>> = snapshotFlow { query.trim() }
        .debounce { if (it.isEmpty()) 0L else SEARCH_DEBOUNCE_MS }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) repository.getCharacters() else repository.searchCharacters(query)
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(newQuery: String) {
        query = newQuery
        savedStateHandle[QUERY_KEY] = newQuery
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}