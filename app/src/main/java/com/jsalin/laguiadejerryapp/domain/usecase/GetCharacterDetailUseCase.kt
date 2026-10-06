package com.jsalin.laguiadejerryapp.domain.usecase

import com.jsalin.laguiadejerryapp.domain.model.CharacterDetail
import com.jsalin.laguiadejerryapp.domain.repository.CharacterRepository
import com.jsalin.laguiadejerryapp.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
) {
    suspend operator fun invoke(characterId: Int): CharacterDetail {
        val character = characterRepository.getCharacter(characterId)
        val episodes = episodeRepository.getEpisodes(character.episodeIds)
        return CharacterDetail(character, episodes)
    }
}