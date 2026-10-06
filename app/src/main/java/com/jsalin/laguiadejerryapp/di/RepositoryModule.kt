package com.jsalin.laguiadejerryapp.di

import com.jsalin.laguiadejerryapp.data.repository.CharacterRepositoryImpl
import com.jsalin.laguiadejerryapp.data.repository.EpisodeRepositoryImpl
import com.jsalin.laguiadejerryapp.domain.repository.CharacterRepository
import com.jsalin.laguiadejerryapp.domain.repository.EpisodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(impl: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @Singleton
    abstract fun bindEpisodeRepository(impl: EpisodeRepositoryImpl): EpisodeRepository

}