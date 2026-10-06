package com.jsalin.laguiadejerryapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jsalin.laguiadejerryapp.data.local.dao.CharacterDao
import com.jsalin.laguiadejerryapp.data.local.dao.EpisodeDao
import com.jsalin.laguiadejerryapp.data.local.dao.RemoteKeyDao
import com.jsalin.laguiadejerryapp.data.local.entity.CharacterEntity
import com.jsalin.laguiadejerryapp.data.local.entity.EpisodeEntity
import com.jsalin.laguiadejerryapp.data.local.entity.RemoteKeyEntity

@Database(
    entities = [CharacterEntity::class, RemoteKeyEntity::class, EpisodeEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class JerryGuideDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun episodeDao(): EpisodeDao
}