package com.jsalin.laguiadejerryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jsalin.laguiadejerryapp.domain.model.CharacterGender
import com.jsalin.laguiadejerryapp.domain.model.CharacterStatus

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String?,
    val gender: CharacterGender,
    val origin: String?,
    val location: String?,
    val imageUrl: String,
    val episodeIds: List<Int>,
)