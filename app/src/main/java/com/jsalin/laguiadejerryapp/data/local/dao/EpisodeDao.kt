package com.jsalin.laguiadejerryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jsalin.laguiadejerryapp.data.local.entity.EpisodeEntity

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episodes WHERE id IN (:ids) ORDER BY id")
    suspend fun getByIds(ids: List<Int>): List<EpisodeEntity>

    @Upsert
    suspend fun upsertAll(episodes: List<EpisodeEntity>)
}