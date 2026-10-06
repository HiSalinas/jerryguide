package com.jsalin.laguiadejerryapp.presentation.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jsalin.laguiadejerryapp.domain.model.DataError
import com.jsalin.laguiadejerryapp.domain.usecase.GetCharacterDetailUseCase
import com.jsalin.laguiadejerryapp.presentation.navigation.CharacterDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCharacterDetail: GetCharacterDetailUseCase,
) : ViewModel() {

    private val characterId = savedStateHandle.toRoute<CharacterDetailRoute>().characterId

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _uiState.value = CharacterDetailUiState.Loading
            _uiState.value = try {
                CharacterDetailUiState.Success(getCharacterDetail(characterId))
            } catch (e: DataError) {
                CharacterDetailUiState.Error(e)
            }
        }
    }
}