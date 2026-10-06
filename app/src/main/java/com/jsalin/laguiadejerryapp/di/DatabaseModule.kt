package com.jsalin.laguiadejerryapp.di

import android.content.Context
import androidx.room.Room
import com.jsalin.laguiadejerryapp.data.local.JerryGuideDatabase
import com.jsalin.laguiadejerryapp.data.local.dao.CharacterDao
import com.jsalin.laguiadejerryapp.data.local.dao.EpisodeDao
import com.jsalin.laguiadejerryapp.data.local.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JerryGuideDatabase =
        Room.databaseBuilder(context, JerryGuideDatabase::class.java, "jerry_guide.db").build()

    @Provides
    fun provideCharacterDao(db: JerryGuideDatabase): CharacterDao = db.characterDao()

    @Provides
    fun provideRemoteKeyDao(db: JerryGuideDatabase): RemoteKeyDao = db.remoteKeyDao()

    @Provides
    fun provideEpisodeDao(db: JerryGuideDatabase): EpisodeDao = db.episodeDao()
}