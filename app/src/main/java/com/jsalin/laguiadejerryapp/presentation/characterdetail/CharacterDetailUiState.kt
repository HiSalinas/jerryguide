package com.jsalin.laguiadejerryapp.presentation.characterdetail

import com.jsalin.laguiadejerryapp.domain.model.CharacterDetail
import com.jsalin.laguiadejerryapp.domain.model.DataError

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Success(val detail: CharacterDetail) : CharacterDetailUiState
    data class Error(val error: DataError) : CharacterDetailUiState
}