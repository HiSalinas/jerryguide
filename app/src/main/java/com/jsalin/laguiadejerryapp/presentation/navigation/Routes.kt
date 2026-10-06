package com.jsalin.laguiadejerryapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CharacterListRoute
@Serializable
data class CharacterDetailRoute(val characterId: Int)